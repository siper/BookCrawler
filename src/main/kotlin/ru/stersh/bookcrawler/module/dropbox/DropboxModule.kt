package ru.stersh.bookcrawler.module.dropbox

import com.dropbox.core.oauth.DbxCredential
import ru.stersh.bookcrawler.Properties
import ru.stersh.bookcrawler.core.BookHandlerManager
import ru.stersh.bookcrawler.core.Module
import ru.stersh.bookcrawler.logger

class DropboxModule : Module() {

    private val accessToken = Properties.get("dropbox.accessToken")
    private val refreshToken = Properties.get("dropbox.refreshToken")
    private val appKey = Properties.get("dropbox.appKey")
    private val appSecret = Properties.get("dropbox.appSecret")
    private val expiresAt = Properties.get("dropbox.expiresAt")?.toLongOrNull() ?: 0
    private val folder = Properties.get("dropbox.storeFolder")

    override val name: String = "dropbox"
    override val bookHandlers: List<BookHandlerManager.BookHandler> = if (hasDropboxCredentials()) {
        val credentials = DbxCredential(
            accessToken,
            expiresAt,
            refreshToken,
            appKey,
            appSecret
        )
        listOf(DropboxBookHandler(credentials, folder))
    } else {
        logger.info("Dropbox module not initialized access token not set")
        emptyList()
    }

    private fun hasDropboxCredentials(): Boolean {
        return refreshToken != null
                && accessToken != null
                && appKey != null
                && appSecret != null
                && folder != null
    }
}