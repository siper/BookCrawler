package ru.stersh.bookcrawler.module.at.api


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Recommendation(
    @SerialName("authorFIO")
    val authorFIO: String,
    @SerialName("authorId")
    val authorId: Int,
    @SerialName("authorUserName")
    val authorUserName: String,
    @SerialName("coAuthorConfirmed")
    val coAuthorConfirmed: Boolean,
    @SerialName("coAuthorFIO")
    val coAuthorFIO: String?,
    @SerialName("coAuthorId")
    val coAuthorId: Long?,
    @SerialName("coAuthorUserName")
    val coAuthorUserName: String?,
    @SerialName("coverUrl")
    val coverUrl: String,
    @SerialName("discount")
    val discount: Float?,
    @SerialName("finishTime")
    val finishTime: String,
    @SerialName("finished")
    val finished: Boolean,
    @SerialName("format")
    val format: String,
    @SerialName("id")
    val id: Int,
    @SerialName("isExclusive")
    val isExclusive: Boolean,
    @SerialName("lastModificationTime")
    val lastModificationTime: String,
    @SerialName("originalAuthor")
    val originalAuthor: String?,
    @SerialName("price")
    val price: Double?,
    @SerialName("removalReason")
    val removalReason: String?,
    @SerialName("removalReasonComment")
    val removalReasonComment: String?,
    @SerialName("secondCoAuthorConfirmed")
    val secondCoAuthorConfirmed: Boolean,
    @SerialName("secondCoAuthorFIO")
    val secondCoAuthorFIO: String?,
    @SerialName("secondCoAuthorId")
    val secondCoAuthorId: Long?,
    @SerialName("secondCoAuthorUserName")
    val secondCoAuthorUserName: String?,
    @SerialName("state")
    val state: String,
    @SerialName("status")
    val status: String,
    @SerialName("title")
    val title: String
)