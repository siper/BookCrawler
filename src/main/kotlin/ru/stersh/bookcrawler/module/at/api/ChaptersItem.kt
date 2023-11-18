package ru.stersh.bookcrawler.module.at.api


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChaptersItem(
    @SerialName("chapters")
    val chapters: List<ChapterMeta>,
    @SerialName("code")
    val code: String?,
    @SerialName("id")
    val id: Int,
    @SerialName("isSuccessful")
    val isSuccessful: Boolean,
    @SerialName("message")
    val message: String?
)