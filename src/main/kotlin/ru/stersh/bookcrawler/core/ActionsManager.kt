package ru.stersh.bookcrawler.core

import java.util.concurrent.CopyOnWriteArrayList

object ActionsManager {
    private val handlers = CopyOnWriteArrayList<ActionHandler>()

    fun addHandler(handler: ActionHandler) {
        handlers.add(handler)
    }

    fun removeHandler(handler: ActionHandler) {
        handlers.remove(handler)
    }

    suspend fun invokeAction(bookId: BookId, action: Action) {
        handlers.forEach {
            it.invokeAction(bookId, action)
        }
    }

    interface ActionHandler {
        val name: String
        suspend fun invokeAction(bookId: BookId, action: Action)
    }
}