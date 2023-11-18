package ru.stersh.bookcrawler.core

fun Book.Image.toBase64(): String {
    return when(this) {
        is Book.Image.Raw -> java.util.Base64.getEncoder().encodeToString(this.bytes)
    }
}