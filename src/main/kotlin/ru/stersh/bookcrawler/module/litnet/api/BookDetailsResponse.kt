package ru.stersh.bookcrawler.module.litnet.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookDetailsResponse(
    @SerialName("series")
    val series: List<SeriesBook>
)