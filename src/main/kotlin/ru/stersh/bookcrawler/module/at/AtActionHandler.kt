package ru.stersh.bookcrawler.module.at

import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import ru.stersh.bookcrawler.module.at.api.At
import ru.stersh.bookcrawler.core.Action
import ru.stersh.bookcrawler.core.ActionsManager
import ru.stersh.bookcrawler.core.BookHandlerManager
import ru.stersh.bookcrawler.core.BookId

class AtActionHandler : ActionsManager.ActionHandler {
    override val name: String = At.PROVIDER_NAME

    override suspend fun invokeAction(bookId: BookId, action: Action) {
        if (bookId.provider != name) return
        when(action) {
            Action.ADD_TO_LIBRARY -> {
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
            }
            Action.MARK_READ -> {
                At.markRead(bookId.id)
                transaction {
                    AtBookDb.update(
                        { AtBookDb.id eq bookId.id.toLong() }
                    ) {
                        it[read] = true
                    }
                }
            }
        }
    }
}