package ru.stersh.bookcrawler.module.at

import kotlinx.coroutines.delay
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import ru.stersh.bookcrawler.Properties
import ru.stersh.bookcrawler.core.*
import ru.stersh.bookcrawler.logger
import ru.stersh.bookcrawler.module.at.api.At
import ru.stersh.bookcrawler.module.at.api.LibraryState
import ru.stersh.bookcrawler.module.at.api.Work
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.time.Duration.Companion.minutes

class AtLibraryCheckTask : TaskManager.Task {
    override val name: String = "at"

    private val libraryCheckDelay = Properties.get(LIBRARY_CHECK_PERIOD)?.toLongOrNull() ?: DELAY

    override suspend fun onInvokeJob() {
        while (true) {
            val remoteLibrary = runCatching { At.library() }
                .onFailure { logger.warn("[AT] Filed to fetch library", it) }
                .getOrNull()
            if (remoteLibrary == null) {
                delay(libraryCheckDelay.minutes)
                continue
            }
            val localLibrary = transaction {
                AtBookDb
                    .selectAll()
                    .map { AtBook.fromRow(it) }
            }
            val newWorkInSeriesIds = remoteLibrary.mapNotNull { it.seriesNextWorkId }

            for (work in remoteLibrary) {
                val localBook = localLibrary.firstOrNull { it.id == work.id }

                val inLibrary = work.inLibraryState != LibraryState.None
                        && work.inLibraryState != LibraryState.Disliked

                if (localBook == null) {
                    insertWorkIntoLibrary(work, inLibrary)
                    onNewWorkInLibrary(work)
                    continue
                }
                if (!localBook.inLibrary && inLibrary) {
                    transaction {
                        AtBookDb.update(
                            { AtBookDb.id eq work.id }
                        ) {
                            it[AtBookDb.inLibrary] = true
                            it[updatedAt] = DateTimeFormatter.ISO_ZONED_DATE_TIME.format(ZonedDateTime.now())
                        }
                    }
                    onNewWorkInLibrary(work)
                    continue
                }
                if (!localBook.purchased && work.isPurchased) {
                    transaction {
                        AtBookDb.update(
                            { AtBookDb.id eq work.id }
                        ) {
                            it[purchased] = true
                            it[updatedAt] = DateTimeFormatter.ISO_ZONED_DATE_TIME.format(ZonedDateTime.now())
                        }
                    }
                    NotificationManager.onNewNotification(
                        Notification(
                            id = BookId(work.id, At.PROVIDER_NAME),
                            title = work.title,
                            coverUrl = work.coverUrl,
                            authors = listOfNotNull(work.authorFIO, work.coAuthorFIO),
                            series = getSeriesFromWork(work),
                            type = MessageType.BOOK_PURCHASED,
                            availableActions = if (work.inLibraryState == LibraryState.Finished) {
                                emptyList()
                            } else {
                                listOf(Action.MARK_READ)
                            }
                        )
                    )
                    handleBook(work.id)
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
                            authors = listOfNotNull(work.authorFIO, work.coAuthorFIO),
                            series = getSeriesFromWork(work),
                            type = MessageType.UPDATES_IN_READING_BOOK,
                            availableActions = if (work.inLibraryState == LibraryState.Finished) {
                                emptyList()
                            } else {
                                listOf(Action.MARK_READ)
                            }
                        )
                    )
                    handleBook(work.id)
                    continue
                }
            }
            for (nextSeriesWorkId in newWorkInSeriesIds) {
                if (localLibrary.none { it.id == nextSeriesWorkId } && remoteLibrary.none { it.id == nextSeriesWorkId }) {
                    val nextWork =
                        runCatching {
                            At.getWork(nextSeriesWorkId)
                        }.onFailure {
                            logger.warn("Filed to receive work info $nextSeriesWorkId")
                        }.getOrNull()
                    if (nextWork == null) {
                        continue
                    }

                    insertWorkIntoLibrary(nextWork, false)
                    NotificationManager.onNewNotification(
                        Notification(
                            id = BookId(nextWork.id, At.PROVIDER_NAME),
                            title = nextWork.title,
                            coverUrl = nextWork.coverUrl,
                            authors = listOfNotNull(nextWork.authorFIO, nextWork.coAuthorFIO),
                            series = getSeriesFromWork(nextWork),
                            type = MessageType.NEW_BOOK_IN_SERIES,
                            availableActions = listOf(Action.ADD_TO_LIBRARY)
                        )
                    )
                }
            }
            delay(libraryCheckDelay.minutes)
        }
    }

    private suspend fun onNewWorkInLibrary(work: Work) {
        NotificationManager.onNewNotification(
            Notification(
                id = BookId(work.id, At.PROVIDER_NAME),
                title = work.title,
                coverUrl = work.coverUrl,
                authors = listOfNotNull(work.authorFIO, work.coAuthorFIO),
                series = getSeriesFromWork(work),
                type = MessageType.NEW_BOOK_IN_LIBRARY,
                availableActions = if (work.inLibraryState == LibraryState.Finished) {
                    emptyList()
                } else {
                    listOf(Action.MARK_READ)
                }
            )
        )
        handleBook(work.id)
    }

    private fun getSeriesFromWork(work: Work): Notification.Series? {
        return if (work.seriesTitle != null && work.seriesOrder != null) {
            Notification.Series(
                title = work.seriesTitle,
                order = work.seriesOrder.inc()
            )
        } else {
            null
        }
    }

    private suspend fun handleBook(workId: Long) {
        runCatching {
            val book = At.getBook(workId)
            BookHandlerManager.onBookCreated(book)
        }.onFailure {
            logger.warn("Filed to download work $workId")
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
            it[purchased] = work.isPurchased
            it[updatedAt] = DateTimeFormatter.ISO_ZONED_DATE_TIME.format(ZonedDateTime.now())
            it[lastModificationTime] = work.lastModificationTime
        }
    }

    companion object {
        private const val DELAY = 10L
        private const val LIBRARY_CHECK_PERIOD = "at.libraryCheckPeriod"
    }
}