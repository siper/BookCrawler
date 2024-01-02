package ru.stersh.bookcrawler.module.litnet.api


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LibInfo(
    @SerialName("add_date")
    val addDate: Int,
    @SerialName("last_chr_count")
    val lastChrCount: Int,
    @SerialName("type")
    val type: Int
) {
    companion object {
        const val TYPE_READING = 0
        const val TYPE_ARCHIVE = 1
        const val TYPE_WANT_READ = 2
    }
}