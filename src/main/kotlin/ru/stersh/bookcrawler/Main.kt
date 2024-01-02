package ru.stersh.bookcrawler

import ru.stersh.bookcrawler.core.ModuleManager
import ru.stersh.bookcrawler.module.at.AtModule
import ru.stersh.bookcrawler.module.dropbox.DropboxModule
import ru.stersh.bookcrawler.module.litnet.LitnetModule
import ru.stersh.bookcrawler.module.local.LocalModule
import ru.stersh.bookcrawler.module.mail.MailModule
import ru.stersh.bookcrawler.module.telegram.TelegramModule


const val CONFIG_FOLDER = "/config"

fun main(args: Array<String>) {
    DatabaseFactory.init()
    ModuleManager.addModule(TelegramModule())
    ModuleManager.addModule(AtModule())
    ModuleManager.addModule(DropboxModule())
    ModuleManager.addModule(LocalModule())
    ModuleManager.addModule(MailModule())
    ModuleManager.addModule(LitnetModule())
}