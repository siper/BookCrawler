package ru.stersh.bookcrawler.module.mail

import ru.stersh.bookcrawler.core.Book
import ru.stersh.bookcrawler.core.BookHandlerManager
import ru.stersh.bookcrawler.core.toFb2
import ru.stersh.bookcrawler.logger
import java.util.*
import javax.activation.DataHandler
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart
import javax.mail.util.ByteArrayDataSource

class MailBookHandler(
    private val smtpSettings: SmtpSettings,
    private val fromAddress: String,
    private val toAddress: String
) : BookHandlerManager.BookHandler {

    override val name: String = "mail"

    private val mailProperties = Properties().apply {
        set("mail.smtp.ssl.protocols", "TLSv1.2")
        set("mail.smtp.host", smtpSettings.host)
        set("mail.smtp.port", smtpSettings.port)
        set("mail.smtp.auth", "true")
        set("mail.smtp.starttls.enable", "true")
        set("mail.smtp.ssl.enable", "true")
    }

    override suspend fun onBookCreated(book: Book) {
        val bookBytes = book
            .toFb2()
            .toByteArray(charset = Charsets.UTF_8)

        val session = Session.getDefaultInstance(mailProperties, object : Authenticator() {

            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(smtpSettings.username, smtpSettings.password)
            }
        })

        session.debug = ru.stersh.bookcrawler.Properties.get("logging.app") in arrayOf("DEBUG", "ALL")

        try {
            val mimeMessage = MimeMessage(session)
            mimeMessage.setFrom(InternetAddress(fromAddress))
            mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toAddress, false))
            mimeMessage.subject = book.title
            mimeMessage.sentDate = Date()

            val messageBodyPart = MimeBodyPart()

            val multipart: Multipart = MimeMultipart()

            val fileName = "${book.title}.fb2"
            val source = ByteArrayDataSource(bookBytes, "application/x-fictionbook")
            messageBodyPart.setDataHandler(DataHandler(source))
            messageBodyPart.fileName = fileName

            multipart.addBodyPart(messageBodyPart)
            mimeMessage.setContent(multipart)

            val smtpTransport = session.getTransport("smtp")
            smtpTransport.connect()
            smtpTransport.sendMessage(mimeMessage, mimeMessage.allRecipients)
            smtpTransport.close()
        } catch (messagingException: MessagingException) {
            logger.warn("[Mail] Filed to send book", messagingException)
        }
    }

    data class SmtpSettings(
        val username: String,
        val password: String,
        val host: String,
        val port: Int
    )
}