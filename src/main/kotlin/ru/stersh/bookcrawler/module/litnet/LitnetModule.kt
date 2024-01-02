package ru.stersh.bookcrawler.module.litnet

import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import ru.stersh.bookcrawler.Properties
import ru.stersh.bookcrawler.core.ActionsManager
import ru.stersh.bookcrawler.core.Module
import ru.stersh.bookcrawler.core.TaskManager
import ru.stersh.bookcrawler.logger

class LitnetModule : Module() {
    private val hasAuth = hasAuth()

    override val name: String = LITNET_PROVIDER_NAME
    override val tasks: List<TaskManager.Task> = if (hasAuth) {
        listOf(LitnetLibraryCheckTask())
    } else {
        emptyList()
    }
    override val actionHandlers: List<ActionsManager.ActionHandler> = if (hasAuth) {
        listOf(LitnetActionHandler())
    } else {
        emptyList()
    }

    init {
        transaction {
            SchemaUtils.create(LitnetBookDb)
        }
        if (!hasAuth) {
            logger.info("Litnet module not initialized, user token not set")
        }
    }

    private fun hasAuth(): Boolean {
        val hasToken = Properties.get("litnet.token") != null
        if (hasToken) {
            return true
        }
        val username = Properties.get("litnet.username")
        val password = Properties.get("litnet.password")
        return username != null && password != null
    }
}