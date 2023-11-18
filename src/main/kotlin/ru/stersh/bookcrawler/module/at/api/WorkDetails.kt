package ru.stersh.bookcrawler.module.at.api


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WorkDetails(
    @SerialName("addedToLibraryTime")
    val addedToLibraryTime: String,
    @SerialName("adultOnly")
    val adultOnly: Boolean,
    @SerialName("afterword")
    val afterword: String?,
    @SerialName("allowDownloads")
    val allowDownloads: Boolean,
    @SerialName("annotation")
    val `annotation`: String,
    @SerialName("atRecommendation")
    val atRecommendation: Boolean,
    @SerialName("authorFIO")
    val authorFIO: String,
    @SerialName("authorId")
    val authorId: Int,
    @SerialName("authorNotes")
    val authorNotes: String?,
    @SerialName("authorUserName")
    val authorUserName: String,
    @SerialName("booktrailerVideoUrl")
    val booktrailerVideoUrl: String?,
    @SerialName("chapters")
    val chapters: List<ChapterInfo>,
    @SerialName("coAuthorConfirmed")
    val coAuthorConfirmed: Boolean,
    @SerialName("coAuthorFIO")
    val coAuthorFIO: String?,
    @SerialName("coAuthorId")
    val coAuthorId: Int?,
    @SerialName("coAuthorUserName")
    val coAuthorUserName: String?,
    @SerialName("collectionCount")
    val collectionCount: Int,
    @SerialName("commentCount")
    val commentCount: Int,
    @SerialName("coverUrl")
    val coverUrl: String,
    @SerialName("discount")
    val discount: Float?,
    @SerialName("downloadErrorCode")
    val downloadErrorCode: String?,
    @SerialName("downloadErrorMessage")
    val downloadErrorMessage: String?,
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
    @SerialName("freeChapterCount")
    val freeChapterCount: Int,
    @SerialName("galleryImages")
    val galleryImages: List<GalleryImage>,
    @SerialName("genreId")
    val genreId: Long,
    @SerialName("id")
    val id: Long,
    @SerialName("inLibraryState")
    val inLibraryState: String,
    @SerialName("isDraft")
    val isDraft: Boolean,
    @SerialName("isExclusive")
    val isExclusive: Boolean,
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
    val likeCount: Int,
    @SerialName("likeTime")
    val likeTime: String?,
    @SerialName("orderId")
    val orderId: Long?,
    @SerialName("orderStatus")
    val orderStatus: String,
    @SerialName("orderStatusMessage")
    val orderStatusMessage: String?,
    @SerialName("originalAuthor")
    val originalAuthor: String?,
    @SerialName("price")
    val price: Double?,
    @SerialName("privacyDisplay")
    val privacyDisplay: String,
    @SerialName("privacyDownloads")
    val privacyDownloads: String,
    @SerialName("promoFragment")
    val promoFragment: Boolean,
    @SerialName("purchaseTime")
    val purchaseTime: String?,
    @SerialName("reciter")
    val reciter: String?,
    @SerialName("recommendations")
    val recommendations: List<Recommendation>,
    @SerialName("reviewCount")
    val reviewCount: Int,
    @SerialName("rewardCount")
    val rewardCount: Int,
    @SerialName("rewardsEnabled")
    val rewardsEnabled: Boolean,
    @SerialName("secondCoAuthorConfirmed")
    val secondCoAuthorConfirmed: Boolean,
    @SerialName("secondCoAuthorFIO")
    val secondCoAuthorFIO: String?,
    @SerialName("secondCoAuthorId")
    val secondCoAuthorId: Long?,
    @SerialName("secondCoAuthorUserName")
    val secondCoAuthorUserName: String?,
    @SerialName("secondSubGenreId")
    val secondSubGenreId: Long?,
    @SerialName("seriesId")
    val seriesId: Long?,
    @SerialName("seriesNextWorkId")
    val seriesNextWorkId: Long?,
    @SerialName("seriesOrder")
    val seriesOrder: Int?,
    @SerialName("seriesTitle")
    val seriesTitle: String?,
    @SerialName("seriesWorkIds")
    val seriesWorkIds: List<Int>,
    @SerialName("seriesWorkNumber")
    val seriesWorkNumber: Int,
    @SerialName("state")
    val state: String,
    @SerialName("status")
    val status: String,
    @SerialName("tags")
    val tags: List<String>,
    @SerialName("textLength")
    val textLength: Int,
    @SerialName("textLengthLastRead")
    val textLengthLastRead: Int,
    @SerialName("title")
    val title: String,
    @SerialName("translator")
    val translator: String?,
    @SerialName("updateInLibraryTime")
    val updateInLibraryTime: String,
    @SerialName("userLikeId")
    val userLikeId: Long?,
    @SerialName("workForm")
    val workForm: String
)