package ru.stersh.bookcrawler.module.dropbox

import ru.stersh.bookcrawler.Properties
import ru.stersh.bookcrawler.core.BookHandlerManager
import ru.stersh.bookcrawler.core.Module
import ru.stersh.bookcrawler.logger

class DropboxModule : Module() {

    private val token = Properties.get("dropbox.token")
    private val folder = Properties.get("dropbox.storeFolder")

    override val name: String = "dropbox"
    override val bookHandlers: List<BookHandlerManager.BookHandler> = if (token != null) {
        listOf(DropboxBookHandler(token, folder))
    } else {
        logger.info("Dropbox module not initialized access token not set")
        emptyList()
    }
}