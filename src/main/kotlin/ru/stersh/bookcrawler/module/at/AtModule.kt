package ru.stersh.bookcrawler.module.at

import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import ru.stersh.bookcrawler.Properties
import ru.stersh.bookcrawler.core.ActionsManager
import ru.stersh.bookcrawler.core.Module
import ru.stersh.bookcrawler.core.TaskManager
import ru.stersh.bookcrawler.logger

class AtModule : Module() {
    private val hasAccessToken = Properties.get("at.accessToken") != null

    override val name: String = "at"
    override val actionHandlers: List<ActionsManager.ActionHandler> = if (hasAccessToken) {
        listOf(AtActionHandler())
    } else {
        emptyList()
    }
    override val tasks: List<TaskManager.Task> = if (hasAccessToken) {
        listOf(AtLibraryCheckTask())
    } else {
        emptyList()
    }

    init {
        transaction {
            SchemaUtils.create(AtBookDb)
        }
        if (!hasAccessToken) {
            logger.info("At module not initialized access token not set")
        }
    }
}