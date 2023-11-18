package ru.stersh.bookcrawler

import java.io.BufferedReader
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.util.Properties

object Properties {
    private val appProps = Properties()

    init {
        val reader = BufferedReader(
            InputStreamReader(
                FileInputStream("$CONFIG_FOLDER/config.properties"), "utf-8")
        )
        appProps.load(reader)
    }

    fun get(key: String): String? {
        return appProps.getProperty(key)
    }

    fun require(key: String): String {
        return requireNotNull(get(key))
    }

    fun write(key: String, value: String) {
        appProps.setProperty(key, value)
        appProps.store(
            FileOutputStream("$CONFIG_FOLDER/config.properties"),
            "Save property: $key with value: $value"
        )
    }
}