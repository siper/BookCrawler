package ru.stersh.bookcrawler.module.at.api


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotificationSettings(
    @SerialName("bell_AudiobooksDiscounts")
    val bellAudiobooksDiscounts: Boolean,
    @SerialName("bell_CollectionUpdates")
    val bellCollectionUpdates: Boolean,
    @SerialName("bell_Group")
    val bellGroup: Boolean,
    @SerialName("bell_MarkAllAsRead")
    val bellMarkAllAsRead: Boolean,
    @SerialName("bell_NewArts")
    val bellNewArts: Boolean,
    @SerialName("bell_NewAudiobooks")
    val bellNewAudiobooks: Boolean,
    @SerialName("bell_NewCommentReplies")
    val bellNewCommentReplies: Boolean,
    @SerialName("bell_NewPosts")
    val bellNewPosts: Boolean,
    @SerialName("bell_NewSubscribers")
    val bellNewSubscribers: Boolean,
    @SerialName("bell_NewUserAwards")
    val bellNewUserAwards: Boolean,
    @SerialName("bell_NewWorks")
    val bellNewWorks: Boolean,
    @SerialName("bell_OnlyUnread")
    val bellOnlyUnread: Boolean,
    @SerialName("bell_SeriesUpdates")
    val bellSeriesUpdates: Boolean,
    @SerialName("bell_WorkDiscounts")
    val bellWorkDiscounts: Boolean,
    @SerialName("bell_WorkFinished")
    val bellWorkFinished: Boolean,
    @SerialName("bell_WorkFinishedSaved")
    val bellWorkFinishedSaved: Boolean,
    @SerialName("bell_WorkPurchases")
    val bellWorkPurchases: Boolean,
    @SerialName("bell_WorkUpdates")
    val bellWorkUpdates: Boolean,
    @SerialName("bell_WorkUpdatesSaved")
    val bellWorkUpdatesSaved: Boolean,
    @SerialName("webPush_AudiobooksDiscounts")
    val webPushAudiobooksDiscounts: Boolean,
    @SerialName("webPush_CollectionUpdates")
    val webPushCollectionUpdates: Boolean,
    @SerialName("webPushFrequency")
    val webPushFrequency: String,
    @SerialName("webPush_NewArts")
    val webPushNewArts: Boolean,
    @SerialName("webPush_NewAudiobooks")
    val webPushNewAudiobooks: Boolean,
    @SerialName("webPush_NewCommentReplies")
    val webPushNewCommentReplies: Boolean,
    @SerialName("webPush_NewComments")
    val webPushNewComments: Boolean,
    @SerialName("webPush_NewPosts")
    val webPushNewPosts: Boolean,
    @SerialName("webPush_NewSubscribers")
    val webPushNewSubscribers: Boolean,
    @SerialName("webPush_NewUserAwards")
    val webPushNewUserAwards: Boolean,
    @SerialName("webPush_NewWorks")
    val webPushNewWorks: Boolean,
    @SerialName("webPush_SeriesUpdates")
    val webPushSeriesUpdates: Boolean,
    @SerialName("webPush_WorkDiscounts")
    val webPushWorkDiscounts: Boolean,
    @SerialName("webPush_WorkFinished")
    val webPushWorkFinished: Boolean,
    @SerialName("webPush_WorkFinishedSaved")
    val webPushWorkFinishedSaved: Boolean,
    @SerialName("webPush_WorkPurchases")
    val webPushWorkPurchases: Boolean,
    @SerialName("webPush_WorkUpdates")
    val webPushWorkUpdates: Boolean,
    @SerialName("webPush_WorkUpdatesSaved")
    val webPushWorkUpdatesSaved: Boolean
)