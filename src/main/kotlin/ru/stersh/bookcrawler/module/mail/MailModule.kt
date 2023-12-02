package ru.stersh.bookcrawler.module.mail

import ru.stersh.bookcrawler.Properties
import ru.stersh.bookcrawler.core.BookHandlerManager
import ru.stersh.bookcrawler.core.Module
import ru.stersh.bookcrawler.logger

class MailModule : Module() {

    private val fromAddress = Properties.get("mail.from")
    private val toAddress = Properties.get("mail.to")
    private val username = Properties.get("mail.smtpUsername")
    private val password = Properties.get("mail.smtpPassword")
    private val host = Properties.get("mail.smtpHost")
    private val port = Properties.get("mail.smtpPort")?.toIntOrNull()

    override val name: String = "mail"
    override val bookHandlers: List<BookHandlerManager.BookHandler> = createBookHandlers()

    private fun createBookHandlers(): List<BookHandlerManager.BookHandler> {
        return if (
            fromAddress != null
            && toAddress != null
            && username != null
            && password != null
            && host != null
            && port != null
        ) {
            val smtpSettings = MailBookHandler.SmtpSettings(
                username = username,
                password = password,
                host = host,
                port = port
            )
            listOf(MailBookHandler(smtpSettings, fromAddress, toAddress))
        } else {
            logger.info("Mail module not initialized some properties not set")
            emptyList()
        }
    }
}