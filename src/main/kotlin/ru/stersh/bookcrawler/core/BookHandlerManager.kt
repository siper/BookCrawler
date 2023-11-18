package ru.stersh.bookcrawler.core

import java.util.concurrent.CopyOnWriteArrayList

object BookHandlerManager {
    private val handlers = CopyOnWriteArrayList<BookHandler>()

    fun addHandler(handler: BookHandler) {
        handlers.add(handler)
    }

    fun removeHandler(handler: BookHandler) {
        handlers.remove(handler)
    }

    suspend fun onBookCreated(book: Book) {
        handlers.forEach {
            it.onBookCreated(book)
        }
    }

    interface BookHandler {
        val name: String
        suspend fun onBookCreated(book: Book)
    }
}