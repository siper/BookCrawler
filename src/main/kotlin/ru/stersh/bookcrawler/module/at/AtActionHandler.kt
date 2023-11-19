package ru.stersh.bookcrawler.module.at

import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import ru.stersh.bookcrawler.core.Action
import ru.stersh.bookcrawler.core.ActionsManager
import ru.stersh.bookcrawler.core.BookHandlerManager
import ru.stersh.bookcrawler.core.BookId
import ru.stersh.bookcrawler.logger
import ru.stersh.bookcrawler.module.at.api.At

class AtActionHandler : ActionsManager.ActionHandler {
    override val name: String = At.PROVIDER_NAME

    override suspend fun invokeAction(bookId: BookId, action: Action) {
        if (bookId.provider != name) return
        when(action) {
            Action.ADD_TO_LIBRARY -> addToLibrary(bookId)
            Action.MARK_READ -> markRead(bookId)
        }
    }

    private suspend fun markRead(bookId: BookId) {
        runCatching {
            At.markRead(bookId.id)
            transaction {
                AtBookDb.update(
                    { AtBookDb.id eq bookId.id }
                ) {
                    it[read] = true
                }
            }
        }.onFailure {
            logger.warn("Filed to mark read book $bookId to library", it)
        }
    }

    private suspend fun addToLibrary(bookId: BookId) {
        runCatching {
            At.addToLibrary(bookId.id)
            transaction {
                AtBookDb.update(
                    { AtBookDb.id eq bookId.id }
                ) {
                    it[inLibrary] = true
                }
            }
            val book = At.getBook(bookId.id)
            BookHandlerManager.onBookCreated(book)
        }.onFailure {
            logger.warn("Filed to add book $bookId to library", it)
        }
    }
}