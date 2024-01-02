package ru.stersh.bookcrawler.module.litnet.api


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChapterText(
    @SerialName("access")
    val access: Boolean,
    @SerialName("id")
    val id: Int,
    @SerialName("text")
    val text: String
)