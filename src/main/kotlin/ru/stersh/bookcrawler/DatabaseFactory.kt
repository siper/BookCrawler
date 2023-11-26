package ru.stersh.bookcrawler

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.StatementContext

object DatabaseFactory {
    fun init() {
        Database.connect(
            url = "jdbc:h2:file:$CONFIG_FOLDER/book_crawler",
            driver = "org.h2.Driver",
            databaseConfig = DatabaseConfig {
                sqlLogger = if (Properties.get("logging.db").toBoolean()) {
                    StdOutSqlLogger
                } else {
                    EmptyDbLogger
                }
            }
        )
    }

    private val EmptyDbLogger = object : SqlLogger {
        override fun log(context: StatementContext, transaction: Transaction) {
        }
    }
}