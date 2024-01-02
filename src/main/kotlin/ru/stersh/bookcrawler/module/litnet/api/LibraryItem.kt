package ru.stersh.bookcrawler.module.litnet.api


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LibraryItem(
    @SerialName("book")
    val book: Book,
    @SerialName("lib_info")
    val libInfo: LibInfo
)