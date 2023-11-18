package ru.stersh.bookcrawler

import org.jetbrains.exposed.sql.Database

object DatabaseFactory {
    fun init() {
        Database.connect(
            url = "jdbc:h2:file:$CONFIG_FOLDER/book_crawler",
            driver = "org.h2.Driver"
        )
    }
}