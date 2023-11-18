package ru.stersh.bookcrawler.module.at

import kotlinx.coroutines.delay
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import ru.stersh.bookcrawler.module.at.api.At
import ru.stersh.bookcrawler.module.at.api.LibraryState
import ru.stersh.bookcrawler.module.at.api.Work
import ru.stersh.bookcrawler.core.*
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class AtLibraryCheckTask : TaskManager.Task {
    override val name: String = "at"

    override suspend fun onInvokeJob() {
        while (true) {
            val localLibrary = transaction {
                AtBookDb
                    .selectAll()
                    .map { AtBook.fromRow(it) }
            }
            val remoteLibrary = At.library()
            val newWorkInSeriesIds = remoteLibrary.mapNotNull { it.seriesNextWorkId }

            for (work in remoteLibrary) {
                val localBook = localLibrary.firstOrNull { it.id == work.id }
                if (localBook == null) {
                    val inLibrary = work.inLibraryState != LibraryState.None && work.inLibraryState != LibraryState.Disliked
                    insertWorkIntoLibrary(work, inLibrary)

                    NotificationManager.onNewNotification(
                        Notification(
                            id = BookId(work.id, At.PROVIDER_NAME),
                            title = work.title,
                            coverUrl = work.coverUrl,
                            author = work.authorFIO,
                            series = work.seriesTitle,
                            type = MessageType.NEW_BOOK_IN_LIBRARY,
                            availableActions = if (work.inLibraryState == LibraryState.Finished) {
                                emptyList()
                            } else {
                                listOf(Action.MARK_READ)
                            }
                        )
                    )
                    val book = At.getBook(work.id)
                    BookHandlerManager.onBookCreated(book)
                    continue
                }
                if (localBook.lastModificationTime != work.lastModificationTime) {
                    transaction {
                        AtBookDb.update(
                            { AtBookDb.id eq work.id }
                        ) {
                            it[completed] = work.isFinished
                            it[coverUrl] = work.coverUrl
                            it[updatedAt] = DateTimeFormatter.ISO_ZONED_DATE_TIME.format(ZonedDateTime.now())
                            it[lastModificationTime] = work.lastModificationTime
                        }
                    }
                    NotificationManager.onNewNotification(
                        Notification(
                            id = BookId(work.id, At.PROVIDER_NAME),
                            title = work.title,
                            coverUrl = work.coverUrl,
                            author = work.authorFIO,
                            series = work.seriesTitle,
                            type = MessageType.UPDATES_IN_READING_BOOK,
                            availableActions = if (work.inLibraryState == LibraryState.Finished) {
                                emptyList()
                            } else {
                                listOf(Action.MARK_READ)
                            }
                        )
                    )
                    val book = At.getBook(work.id)
                    BookHandlerManager.onBookCreated(book)
                    continue
                }
            }
            for (nextSeriesWorkId in newWorkInSeriesIds) {
                if (localLibrary.none { it.id == nextSeriesWorkId } && remoteLibrary.none { it.id == nextSeriesWorkId }) {
                    val nextWork = At.getWork(nextSeriesWorkId)
                    insertWorkIntoLibrary(nextWork, false)
                    NotificationManager.onNewNotification(
                        Notification(
                            id = BookId(nextWork.id, At.PROVIDER_NAME),
                            title = nextWork.title,
                            coverUrl = nextWork.coverUrl,
                            author = nextWork.authorFIO,
                            series = nextWork.seriesTitle,
                            type = MessageType.NEW_BOOK_IN_SERIES,
                            availableActions = listOf(Action.ADD_TO_LIBRARY)
                        )
                    )
                }
            }
            delay(DELAY)
        }
    }

    private fun insertWorkIntoLibrary(work: Work, inLibrary: Boolean) = transaction {
        AtBookDb.insert {
            it[title] = work.title
            it[series] = work.seriesTitle
            it[author] = work.authorFIO
            it[seriesId] = work.seriesId
            it[authorId] = work.authorId
            it[completed] = work.isFinished
            it[coverUrl] = work.coverUrl
            it[AtBookDb.inLibrary] = inLibrary
            it[read] = work.inLibraryState == LibraryState.Finished
            it[id] = work.id
            it[updatedAt] = DateTimeFormatter.ISO_ZONED_DATE_TIME.format(ZonedDateTime.now())
            it[lastModificationTime] = work.lastModificationTime
        }
    }

    companion object {
        private const val DELAY = 10 * 60 * 1000L
    }
}