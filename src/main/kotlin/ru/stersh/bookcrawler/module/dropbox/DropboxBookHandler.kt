package ru.stersh.bookcrawler.module.dropbox

import com.dropbox.core.DbxRequestConfig
import com.dropbox.core.v2.DbxClientV2
import com.dropbox.core.v2.files.WriteMode
import ru.stersh.bookcrawler.core.Book
import ru.stersh.bookcrawler.core.BookHandlerManager
import ru.stersh.bookcrawler.core.toFb2

class DropboxBookHandler(
    private val token: String,
    private val folder: String?
) : BookHandlerManager.BookHandler {

    private val config = DbxRequestConfig
        .newBuilder("BookCrawler")
        .build()
    private val client = DbxClientV2(config, token)

    override val name: String = "dropbox"

    override suspend fun onBookCreated(book: Book) {
        val fb2Book = book.toFb2()
        val ins = fb2Book.byteInputStream()

        val bookFilename = "${book.title}.fb2"
        val targetPath = if (folder == null) {
            bookFilename
        } else {
            "${folder}/${bookFilename}"
        }

        client
            .files()
            .uploadBuilder(targetPath)
            .withMode(WriteMode.OVERWRITE)
            .uploadAndFinish(ins)
    }
}