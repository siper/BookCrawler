package ru.stersh.bookcrawler.module.litnet

import kotlinx.coroutines.delay
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import ru.stersh.bookcrawler.Properties
import ru.stersh.bookcrawler.core.*
import ru.stersh.bookcrawler.logger
import ru.stersh.bookcrawler.module.litnet.api.*
import ru.stersh.bookcrawler.module.litnet.api.Book
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.time.Duration.Companion.minutes

class LitnetLibraryCheckTask : TaskManager.Task {
    override val name: String = LITNET_PROVIDER_NAME

    private val libraryCheckDelay = Properties.get("litnet.libraryCheckPeriod")?.toLongOrNull() ?: DELAY

    override suspend fun onInvokeJob() {
        val authResult = runCatching { Litnet.checkAuth() }
            .onFailure { logger.warn("[Litnet] Filed to login, fix auth credentials and restart app") }
        if (authResult.isFailure) {
            return
        }
        while (true) {
            val remoteLibrary = runCatching { Litnet.library() }
                .onFailure { logger.warn("[Litnet] Filed to fetch library") }
                .getOrNull()
            if (remoteLibrary == null) {
                delay(libraryCheckDelay.minutes)
                continue
            }

            checkNewBooks(remoteLibrary)
            checkBooksInSeries()

            delay(libraryCheckDelay.minutes)
        }
    }

    private suspend fun checkNewBooks(remoteLibrary: List<LibraryItem>) {
        val localLibrary = transaction {
            LitnetBookDb
                .selectAll()
                .map { LitnetBook.fromRow(it) }
        }
        for (libraryItem in remoteLibrary) {
            val localBook = localLibrary.firstOrNull { it.id == libraryItem.book.id }

            if (localBook == null) {
                val isRead = libraryItem.libInfo.type == LibInfo.TYPE_ARCHIVE
                val inSeries = libraryItem.book.priorityCycle != 0
                insertBookIntoLibrary(libraryItem.book, inSeries, isRead)
                NotificationManager.onNewNotification(
                    Notification(
                        id = BookId(libraryItem.book.id, LITNET_PROVIDER_NAME),
                        title = libraryItem.book.title,
                        coverUrl = libraryItem.book.cover,
                        authors = listOfNotNull(libraryItem.book.authorName, libraryItem.book.coAuthorName),
                        series = null,
                        type = MessageType.NEW_BOOK_IN_LIBRARY,
                        availableActions = if (libraryItem.libInfo.type == LibInfo.TYPE_ARCHIVE) {
                            emptyList()
                        } else {
                            listOf(Action.MARK_READ)
                        }
                    )
                )
                handleBook(libraryItem.book.id)
                continue
            }

            if (localBook.lastModificationTime != libraryItem.book.lastUpdate) {
                transaction {
                    LitnetBookDb.update(
                        { LitnetBookDb.id eq libraryItem.book.id }
                    ) {
                        it[purchased] = libraryItem.book.isPurchased
                        it[completed] = libraryItem.book.status == "fulltext"
                        it[coverUrl] = libraryItem.book.cover
                        it[updatedAt] = DateTimeFormatter.ISO_ZONED_DATE_TIME.format(ZonedDateTime.now())
                        it[lastModificationTime] = libraryItem.book.lastUpdate
                    }
                }

                NotificationManager.onNewNotification(
                    Notification(
                        id = BookId(libraryItem.book.id, LITNET_PROVIDER_NAME),
                        title = libraryItem.book.title,
                        coverUrl = libraryItem.book.cover,
                        authors = listOfNotNull(libraryItem.book.authorName, libraryItem.book.coAuthorName),
                        series = null,
                        type = MessageType.UPDATES_IN_READING_BOOK,
                        availableActions = if (libraryItem.libInfo.type == LibInfo.TYPE_ARCHIVE) {
                            emptyList()
                        } else {
                            listOf(Action.MARK_READ)
                        }
                    )
                )
                handleBook(libraryItem.book.id)
                continue
            }
        }
    }

    private suspend fun checkBooksInSeries() {
        val localLibraryIds = transaction {
            LitnetBookDb
                .select { LitnetBookDb.inSeries eq true }
                .map { it[LitnetBookDb.id] }
        }

        val newBookIds = mutableSetOf<Long>()

        for (localId in localLibraryIds) {
            val allSeriesBookResult = runCatching { Litnet.getAllSeriesBooks(localId) }
                .onFailure {
                    logger.warn("[Litnet] Filed to get all series books for $localId")
                }

            val allSeriesBookIds = allSeriesBookResult
                .getOrNull()
                ?.map { it.id }
                ?: continue
            for (bookId in allSeriesBookIds) {
                if (bookId !in localLibraryIds) {
                    newBookIds.add(bookId)
                }
            }
        }

        for (newBookId in newBookIds) {
            val bookDetailsResult = runCatching { Litnet.getBookDetails(newBookId) }
                .onFailure {
                    logger.warn("[Litnet] Filed to fetch book details $newBookId")
                }
            val bookDetails = bookDetailsResult.getOrNull() ?: continue

            insertBookIntoLibrary(bookDetails)
            NotificationManager.onNewNotification(
                Notification(
                    id = BookId(bookDetails.id, LITNET_PROVIDER_NAME),
                    title = bookDetails.title,
                    coverUrl = bookDetails.cover,
                    authors = listOfNotNull(bookDetails.authorName, bookDetails.coAuthorName),
                    series = null,
                    type = MessageType.NEW_BOOK_IN_SERIES,
                    availableActions = listOf(Action.ADD_TO_LIBRARY)
                )
            )
        }
    }

    private suspend fun handleBook(bookId: Long) {
        runCatching {
            val book = Litnet.getBook(bookId)
            BookHandlerManager.onBookCreated(book)
        }.onFailure {
            logger.warn("[Litnet] Filed to download book $bookId")
        }
    }

    private fun insertBookIntoLibrary(
        book: Book,
        inSeries: Boolean,
        isRead: Boolean
    ) = transaction {
        LitnetBookDb.insert {
            it[title] = book.title
            it[LitnetBookDb.inSeries] = inSeries
            it[author] = book.authorName
            it[authorId] = book.authorId
            it[completed] = book.status == "fulltext"
            it[coverUrl] = book.cover
            it[purchased] = book.isPurchased
            it[read] = isRead
            it[id] = book.id
            it[inLibrary] = true
            it[updatedAt] = DateTimeFormatter.ISO_ZONED_DATE_TIME.format(ZonedDateTime.now())
            it[lastModificationTime] = book.lastUpdate
        }
    }

    private fun insertBookIntoLibrary(bookDetails: BookDetails) = transaction {
        LitnetBookDb.insert {
            it[title] = bookDetails.title
            it[inSeries] = true
            it[author] = bookDetails.authorName
            it[authorId] = bookDetails.authorId
            it[completed] = bookDetails.status == "fulltext"
            it[coverUrl] = bookDetails.cover
            it[purchased] = bookDetails.isPurchased
            it[read] = false
            it[id] = bookDetails.id
            it[inLibrary] = false
            it[updatedAt] = DateTimeFormatter.ISO_ZONED_DATE_TIME.format(ZonedDateTime.now())
            it[lastModificationTime] = bookDetails.lastUpdate
        }
    }

    companion object {
        private const val DELAY = 10L
    }
}