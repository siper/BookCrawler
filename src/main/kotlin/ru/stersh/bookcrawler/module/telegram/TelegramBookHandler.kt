package ru.stersh.bookcrawler.module.telegram

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.TelegramFile
import ru.stersh.bookcrawler.core.Book
import ru.stersh.bookcrawler.core.BookHandlerManager
import ru.stersh.bookcrawler.core.toFb2
import ru.stersh.bookcrawler.module.at.normalizeBookName

class TelegramBookHandler(
    private val bot: Bot,
    private val chatId: Long
) : BookHandlerManager.BookHandler {
    override val name: String = "telegram"

    override suspend fun onBookCreated(book: Book) {
        bot.sendDocument(
            chatId = ChatId.fromId(chatId),
            document = TelegramFile.ByByteArray(book.toFb2().encodeToByteArray(), filename = "${normalizeBookName(book.title)}.fb2")
        )
    }
}