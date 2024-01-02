package ru.stersh.bookcrawler.module.litnet.api


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Chapter(
    @SerialName("access")
    val access: Boolean,
    @SerialName("active")
    val active: Boolean,
    @SerialName("book_id")
    val bookId: Int,
    @SerialName("chr_length")
    val chrLength: Int,
    @SerialName("create_date")
    val createDate: Long,
    @SerialName("id")
    val id: Long,
    @SerialName("last_update")
    val lastUpdate: Long,
    @SerialName("pages_count")
    val pagesCount: Int,
    @SerialName("priority")
    val priority: Int,
    @SerialName("title")
    val title: String
)