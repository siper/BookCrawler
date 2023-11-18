package ru.stersh.bookcrawler.core

import java.util.concurrent.CopyOnWriteArrayList

object NotificationManager {
    private val handlers = CopyOnWriteArrayList<NotificationHandler>()

    fun addHandler(handler: NotificationHandler) {
        handlers.add(handler)
    }

    fun removeHandler(handler: NotificationHandler) {
        handlers.remove(handler)
    }

    suspend fun onNewNotification(notification: Notification) {
        handlers.forEach {
            it.onNewNotification(notification)
        }
    }

    interface NotificationHandler {
        val name: String
        suspend fun onNewNotification(notification: Notification)
    }
}