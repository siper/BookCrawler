package ru.stersh.bookcrawler.module.at.api


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LibraryResult(
    @SerialName("finishedCount")
    val finishedCount: Int,
    @SerialName("purchasedCount")
    val purchasedCount: Int,
    @SerialName("readingCount")
    val readingCount: Int,
    @SerialName("savedCount")
    val savedCount: Int,
    @SerialName("totalCount")
    val totalCount: Int,
    @SerialName("worksInLibrary")
    val worksInLibrary: List<Work>
)