package ru.stersh.bookcrawler.module.at

import ru.homyakin.iuliia.Schemas
import ru.homyakin.iuliia.Translator

fun getDecodedText(userId: String, key: String, text: String): String {
    val secret = key.reversed() + "@_@" + userId
    val stringBuilder = StringBuilder()
    text.forEachIndexed { index, c ->
        stringBuilder.append(
            (c.code xor secret[index % secret.length].code).toChar()
        )
    }
    return stringBuilder.toString()
}

private val StopChars = listOf('?', '!', '/', '\\','*', ':', '<', '>', '"', '|', '+')

fun normalizeBookName(source: String): String {
    val translator = Translator(Schemas.WIKIPEDIA)
    var newName = translator.translate(source)
    for (char in StopChars) {
        newName = newName.replace(char.toString(), "")
    }
    return newName
}