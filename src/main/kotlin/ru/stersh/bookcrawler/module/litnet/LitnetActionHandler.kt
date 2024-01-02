package ru.stersh.bookcrawler.module.litnet

import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import ru.stersh.bookcrawler.core.Action
import ru.stersh.bookcrawler.core.ActionsManager
import ru.stersh.bookcrawler.core.BookHandlerManager
import ru.stersh.bookcrawler.core.BookId
import ru.stersh.bookcrawler.logger
import ru.stersh.bookcrawler.module.litnet.api.Litnet

class LitnetActionHandler : ActionsManager.ActionHandler {
    override val name: String = LITNET_PROVIDER_NAME

    override suspend fun invokeAction(bookId: BookId, action: Action) {
        if (bookId.provider != name) return
        when(action) {
            Action.ADD_TO_LIBRARY -> addToLibrary(bookId)
            Action.MARK_READ -> markRead(bookId)
        }
    }

    private suspend fun markRead(bookId: BookId) {
        runCatching {
            Litnet.markRead(bookId.id)
            transaction {
                LitnetBookDb.update(
                    { LitnetBookDb.id eq bookId.id }
                ) {
                    it[read] = true
                }
            }
        }.onFailure {
            logger.warn("[Litnet] Filed to mark read book $bookId in library", it)
        }
    }

    private suspend fun addToLibrary(bookId: BookId) {
        runCatching {
            Litnet.addToLibrary(bookId.id)
            transaction {
                LitnetBookDb.update(
                    { LitnetBookDb.id eq bookId.id }
                ) {
                    it[inLibrary] = true
                }
            }
            val book = Litnet.getBook(bookId.id)
            BookHandlerManager.onBookCreated(book)
        }.onFailure {
            logger.warn("[Litnet] Filed to add book $bookId to library", it)
        }
    }
}