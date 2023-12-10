package ru.stersh.bookcrawler.module.local

import ru.stersh.bookcrawler.core.Book
import ru.stersh.bookcrawler.core.BookHandlerManager
import ru.stersh.bookcrawler.core.toFb2
import ru.stersh.bookcrawler.logger
import ru.stersh.bookcrawler.module.at.normalizeBookName
import java.io.File

class LocalBookHandler(
    private val folder: String
) : BookHandlerManager.BookHandler {

    override val name: String = "local"

    override suspend fun onBookCreated(book: Book) {
        val fb2Book = book.toFb2()
        val bookFilename = "${normalizeBookName(book.title)}.fb2"

        try {
            File(folder, bookFilename).writeText(fb2Book)
        } catch (e: Exception) {
            logger.warn("Filed to save book $bookFilename", e)
        }
    }
}