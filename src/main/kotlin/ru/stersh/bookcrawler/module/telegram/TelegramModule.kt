package ru.stersh.bookcrawler.module.telegram

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.callbackQuery
import com.github.kotlintelegrambot.entities.ChatId
import kotlinx.coroutines.*
import ru.stersh.bookcrawler.Properties
import ru.stersh.bookcrawler.core.*
import ru.stersh.bookcrawler.logger

class TelegramModule : Module() {
    private val telegramScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var botRunJob: Job? = null

    private val chatId = Properties.get("telegram.chatId")?.toLongOrNull()
    private val botToken = Properties.get("telegram.botToken")
    private val uploadBookUpdates = Properties.get("telegram.uploadBookUpdates")?.toBoolean() ?: false

    private val bot = createBot()
    private val notificationHandler = if (bot != null && chatId != null) {
        TelegramNotificationHandler(bot, chatId)
    } else {
        logger.info("Telegram module not initialized bot token or chatId not set")
        null
    }
    private val bookHandler = if (bot != null && chatId != null && uploadBookUpdates) {
        TelegramBookHandler(bot, chatId)
    } else {
        null
    }

    override val name: String = "telegram"
    override val notificationHandlers: List<NotificationManager.NotificationHandler> = listOfNotNull(notificationHandler)
    override val bookHandlers: List<BookHandlerManager.BookHandler> = listOfNotNull(bookHandler)

    init {
        if (bot != null) {
            botRunJob = telegramScope.launch {
                runBot()
            }
        }
    }

    override fun release() {
        botRunJob?.cancel()
        botRunJob = null
    }

    private suspend fun runBot() = suspendCancellableCoroutine<Unit> {
        bot?.startPolling()

        it.invokeOnCancellation {
            bot?.stopPolling()
        }
    }

    private fun parseBookId(data: String): BookId? {
        val split = data.split("_")
        if (split.size < 4) {
            println("Split size not 4 for callback query")
            return null
        }
        val provider = split[2]
        val id = split[3].toLongOrNull() ?: return null
        return BookId(id, provider)
    }

    private fun createBot(): Bot? {
        if (chatId == null || botToken == null) {
            return null
        }
        return bot {
            token = botToken
            dispatch {
                callbackQuery("library_add") {
                    val bookId = parseBookId(callbackQuery.data) ?: return@callbackQuery
                    logger.info("Trying add to library book: $bookId")
                    ActionsManager.invokeAction(bookId, Action.ADD_TO_LIBRARY)
                    bot.editMessageReplyMarkup(
                        chatId = ChatId.fromId(chatId),
                        messageId = callbackQuery.message?.messageId,
                        inlineMessageId = callbackQuery.inlineMessageId,
                        replyMarkup = null
                    )
                }
                callbackQuery("mark_read") {
                    val bookId = parseBookId(callbackQuery.data) ?: return@callbackQuery
                    logger.info("Trying mark read book: $bookId")
                    ActionsManager.invokeAction(bookId, Action.MARK_READ)
                    bot.editMessageReplyMarkup(
                        chatId = ChatId.fromId(chatId),
                        messageId = callbackQuery.message?.messageId,
                        inlineMessageId = callbackQuery.inlineMessageId,
                        replyMarkup = null
                    )
                }
            }
        }
    }
}