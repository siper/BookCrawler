package ru.stersh.bookcrawler.module.telegram

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.*
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton
import ru.stersh.bookcrawler.core.*

class TelegramNotificationHandler(
    private val bot: Bot,
    private val chatId: Long
) : NotificationManager.NotificationHandler {

    override val name: String = "telegram"

    override suspend fun onNewNotification(notification: Notification) {
        val title = when (notification.type) {
            MessageType.NEW_BOOK_IN_LIBRARY -> "Новая книга в библиотеке"
            MessageType.UPDATES_IN_READING_BOOK -> "Обновление книги"
            MessageType.NEW_BOOK_IN_SERIES -> "Новая книга в серии"
            MessageType.BOOK_PURCHASED -> "Книга куплена"
        }
        sendUpdates(title, notification)
    }

    private fun sendUpdates(title: String, notification: Notification) {
        val message = StringBuilder().apply {
            append(title)
            append("\n")
            append("\n")
            append("*${notification.title}*")
            append("\n")
        }
        message.append("_")
        notification.authors.forEachIndexed { index, author ->
            if (index > 0) {
                message.append(", ")
            }
            message.append(author)
        }
        message.append("_")
        if (notification.series != null) {
            message.append("\n")
            message.append("__${notification.series.title} (${notification.series.order})__")
        }

        bot.sendPhoto(
            chatId = ChatId.fromId(chatId),
            photo = TelegramFile.ByUrl(notification.coverUrl),
            caption = message.toString(),
            parseMode = ParseMode.MARKDOWN,
            replyMarkup = createReplyMarkup(notification.id, notification.availableActions)
        )
    }

    private fun createReplyMarkup(bookId: BookId, actions: List<Action>): ReplyMarkup? {
        if (actions.isEmpty()) {
            return null
        }
        return actions
            .map {
                when (it) {
                    Action.ADD_TO_LIBRARY -> {
                        InlineKeyboardButton.CallbackData(
                            text = "Добавить в библиотеку",
                            callbackData = "library_add_${bookId.provider}_${bookId.id}"
                        )
                    }

                    Action.MARK_READ -> {
                        InlineKeyboardButton.CallbackData(
                            text = "Отметить прочитанным",
                            callbackData = "mark_read_${bookId.provider}_${bookId.id}"
                        )
                    }
                }
            }
            .let {
                InlineKeyboardMarkup.create(it)
            }
    }
}