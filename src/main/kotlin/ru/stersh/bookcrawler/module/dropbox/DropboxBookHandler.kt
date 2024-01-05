package ru.stersh.bookcrawler.module.dropbox

import com.dropbox.core.DbxRequestConfig
import com.dropbox.core.oauth.DbxCredential
import com.dropbox.core.v2.DbxClientV2
import com.dropbox.core.v2.files.WriteMode
import ru.stersh.bookcrawler.Properties
import ru.stersh.bookcrawler.core.Book
import ru.stersh.bookcrawler.core.BookHandlerManager
import ru.stersh.bookcrawler.core.toFb2
import ru.stersh.bookcrawler.logger
import ru.stersh.bookcrawler.module.at.normalizeBookName

class DropboxBookHandler(
    private val credentials: DbxCredential,
    private val folder: String?
) : BookHandlerManager.BookHandler {

    private val config = DbxRequestConfig
        .newBuilder("BookCrawler")
        .build()
    private val client = DbxClientV2(config, credentials)

    override val name: String = "dropbox"

    override suspend fun onBookCreated(book: Book) {
        val fb2Book = book.toFb2()
        val ins = fb2Book.byteInputStream()

        val bookFilename = "${normalizeBookName(book.title)}.fb2"
        val targetPath = if (folder == null) {
            bookFilename
        } else {
            "${folder}/${bookFilename}"
        }

        try {
            client
                .files()
                .uploadBuilder(targetPath)
                .withMode(WriteMode.OVERWRITE)
                .uploadAndFinish(ins)
            updateCredentialsIfNeed()
        } catch (e: Exception) {
            logger.warn("Filed upload book on dropbox", e)
        }
    }

    private fun updateCredentialsIfNeed() {
        val accessToken = Properties.get("dropbox.accessToken")
        if (accessToken != credentials.accessToken) {
            Properties.write("dropbox.accessToken", credentials.accessToken)
        }
        val refreshToken = Properties.get("dropbox.refreshToken")
        if (refreshToken != credentials.refreshToken) {
            Properties.write("dropbox.refreshToken", credentials.refreshToken)
        }
        val expiresAt = Properties.get("dropbox.expiresAt")?.toLongOrNull() ?: 0
        if (expiresAt != credentials.expiresAt) {
            Properties.write("dropbox.expiresAt", credentials.expiresAt.toString())
        }
    }
}