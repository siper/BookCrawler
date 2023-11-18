package ru.stersh.bookcrawler.module.at.api


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LibraryState(
    @SerialName("state")
    val state: String,
    @SerialName("workIds")
    val workIds: List<Long>
) {
    companion object {
        const val None = "None"
        const val Reading = "Reading"
        const val Saved = "Saved"
        const val Finished = "Finished"
        const val Disliked = "Disliked"
    }
}