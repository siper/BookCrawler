package ru.stersh.bookcrawler.module.local

import ru.stersh.bookcrawler.Properties
import ru.stersh.bookcrawler.core.BookHandlerManager
import ru.stersh.bookcrawler.core.Module
import ru.stersh.bookcrawler.logger
import java.io.File

class LocalModule : Module() {

    private val folderPath = Properties.get("local.storeFolder")

    override val name: String = "local"
    override val bookHandlers: List<BookHandlerManager.BookHandler> = if (folderPath != null && folderReady()) {
        listOf(LocalBookHandler(folderPath))
    } else {
        logger.info("Local module not initialized, folder not set")
        emptyList()
    }

    private fun folderReady(): Boolean {
        if (folderPath == null) {
            return false
        }

        val folder = File(folderPath)
        if (folder.exists() && folder.canWrite()) {
            return true
        }

        try {
            folder.mkdirs()
            return true
        } catch (e: SecurityException) {
            logger.warn("Cannot create folders: ${folder.absolutePath}, no permissions")
        }
        return false
    }
}