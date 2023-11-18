package ru.stersh.bookcrawler.module.at.api


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChapterInfo(
    @SerialName("id")
    val id: Long,
    @SerialName("isAvailable")
    val isAvailable: Boolean,
    @SerialName("isDraft")
    val isDraft: Boolean,
    @SerialName("lastModificationTime")
    val lastModificationTime: String,
    @SerialName("publishTime")
    val publishTime: String,
    @SerialName("sortOrder")
    val sortOrder: Int,
    @SerialName("textLength")
    val textLength: Int,
    @SerialName("title")
    val title: String,
    @SerialName("workId")
    val workId: Long
)