package ru.stersh.bookcrawler.module.at.api


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Work(
    @SerialName("addedToLibraryTime")
    val addedToLibraryTime: String,
    @SerialName("adultOnly")
    val adultOnly: Boolean,
    @SerialName("afterword")
    val afterword: String?,
    @SerialName("authorFIO")
    val authorFIO: String,
    @SerialName("authorId")
    val authorId: Long,
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
    @SerialName("commentCount")
    val commentCount: Long,
    @SerialName("coverUrl")
    val coverUrl: String,
    @SerialName("enableRedLine")
    val enableRedLine: Boolean,
    @SerialName("enableTTS")
    val enableTTS: Boolean,
    @SerialName("finishTime")
    val finishTime: String?,
    @SerialName("firstSubGenreId")
    val firstSubGenreId: Long,
    @SerialName("format")
    val format: String,
    @SerialName("genreId")
    val genreId: Long,
    @SerialName("id")
    val id: Long,
    @SerialName("inLibraryState")
    val inLibraryState: String,
    @SerialName("isDraft")
    val isDraft: Boolean,
    @SerialName("isFinished")
    val isFinished: Boolean,
    @SerialName("isPurchased")
    val isPurchased: Boolean,
    @SerialName("lastChapterId")
    val lastChapterId: Long?,
    @SerialName("lastChapterProgress")
    val lastChapterProgress: Double?,
    @SerialName("lastModificationTime")
    val lastModificationTime: String,
    @SerialName("lastReadTime")
    val lastReadTime: String?,
    @SerialName("lastUpdateTime")
    val lastUpdateTime: String,
    @SerialName("likeCount")
    val likeCount: Long,
    @SerialName("likeTime")
    val likeTime: String?,
    @SerialName("price")
    val price: Double?,
    @SerialName("privacyDisplay")
    val privacyDisplay: String,
    @SerialName("purchaseTime")
    val purchaseTime: String?,
    @SerialName("rewardCount")
    val rewardCount: Long,
    @SerialName("rewardsEnabled")
    val rewardsEnabled: Boolean,
    @SerialName("secondCoAuthorConfirmed")
    val secondCoAuthorConfirmed: Boolean,
    @SerialName("secondSubGenreId")
    val secondSubGenreId: Long?,
    @SerialName("seriesId")
    val seriesId: Long,
    @SerialName("seriesNextWorkId")
    val seriesNextWorkId: Long?,
    @SerialName("seriesOrder")
    val seriesOrder: Long,
    @SerialName("seriesTitle")
    val seriesTitle: String,
    @SerialName("state")
    val state: String,
    @SerialName("status")
    val status: String,
    @SerialName("textLength")
    val textLength: Long,
    @SerialName("textLengthLastRead")
    val textLengthLastRead: Long,
    @SerialName("title")
    val title: String,
    @SerialName("updateInLibraryTime")
    val updateInLibraryTime: String,
    @SerialName("userLikeId")
    val userLikeId: Long?,
    @SerialName("workForm")
    val workForm: String
)