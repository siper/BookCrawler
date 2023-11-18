package ru.stersh.bookcrawler.module.at

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