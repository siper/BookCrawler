package ru.stersh.bookcrawler.core

abstract class Module {
    abstract val name: String
    open val actionHandlers: List<ActionsManager.ActionHandler> = emptyList()
    open val notificationHandlers: List<NotificationManager.NotificationHandler> = emptyList()
    open val bookHandlers: List<BookHandlerManager.BookHandler> = emptyList()
    open val tasks: List<TaskManager.Task> = emptyList()

    open fun release() {}
}