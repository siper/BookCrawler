package ru.stersh.bookcrawler.module.litnet.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SeriesBook(
    @SerialName("id")
    val id: Long,
    @SerialName("cover")
    val cover: String,
    @SerialName("title")
    val title: String,
    @SerialName("index")
    val index: Int
)