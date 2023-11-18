package ru.stersh.bookcrawler.core

import java.util.concurrent.CopyOnWriteArrayList

object ModuleManager {
    private val modules = CopyOnWriteArrayList<Module>()

    fun addModule(module: Module) {
        if (module in modules) {
            return
        }
        modules.add(module)
        module.actionHandlers.forEach {
            ActionsManager.addHandler(it)
        }
        module.notificationHandlers.forEach {
            NotificationManager.addHandler(it)
        }
        module.bookHandlers.forEach {
            BookHandlerManager.addHandler(it)
        }
        module.tasks.forEach {
            TaskManager.addTask(it)
        }
    }

    fun removeModule(module: Module) {
        if (module !in modules) {
            return
        }
        modules.remove(module)
        module.actionHandlers.forEach {
            ActionsManager.removeHandler(it)
        }
        module.notificationHandlers.forEach {
            NotificationManager.removeHandler(it)
        }
        module.bookHandlers.forEach {
            BookHandlerManager.removeHandler(it)
        }
        module.tasks.forEach {
            TaskManager.removeTask(it)
        }
    }
}