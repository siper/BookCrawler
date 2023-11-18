package ru.stersh.bookcrawler.module.at.api


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Chapter(
    @SerialName("id")
    val id: Int,
    @SerialName("isDraft")
    val isDraft: Boolean,
    @SerialName("key")
    val key: String?,
    @SerialName("lastModificationTime")
    val lastModificationTime: String,
    @SerialName("publishTime")
    val publishTime: String,
    @SerialName("isSuccessful")
    val isSuccessful: Boolean,
    @SerialName("text")
    val text: String?,
    @SerialName("title")
    val title: String
)