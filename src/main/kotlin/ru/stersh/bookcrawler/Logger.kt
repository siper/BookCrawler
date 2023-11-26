package ru.stersh.bookcrawler

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import io.ktor.client.plugins.logging.*
import org.slf4j.LoggerFactory

val NETWORK_LOG_LEVEL = Properties.get("logging.network")?.let { LogLevel.valueOf(it) } ?: LogLevel.NONE

private val LOG_LEVEL = Properties.get("logging.app")?.let { Level.toLevel(it) } ?: Level.INFO

val Any.logger: Logger
    get() = (LoggerFactory.getLogger(this::class.java) as Logger).apply {
        level = LOG_LEVEL
    }
