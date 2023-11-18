package ru.stersh.bookcrawler.module.at.api


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrentUser(
    @SerialName("avatarUrl")
    val avatarUrl: String?,
    @SerialName("backgroundUrl")
    val backgroundUrl: String?,
    @SerialName("banEnd")
    val banEnd: String?,
    @SerialName("banReason")
    val banReason: String?,
    @SerialName("birthday")
    val birthday: String?,
    @SerialName("disableSeriesAfterword")
    val disableSeriesAfterword: Boolean,
    @SerialName("email")
    val email: String,
    @SerialName("emailConfirmed")
    val emailConfirmed: Boolean,
    @SerialName("fio")
    val fio: String,
    @SerialName("hideDislike")
    val hideDislike: Boolean,
    @SerialName("hideFinished")
    val hideFinished: Boolean,
    @SerialName("id")
    val id: Int,
    @SerialName("ignoredGenreIds")
    val ignoredGenreIds: List<Int>,
    @SerialName("isBanned")
    val isBanned: Boolean,
    @SerialName("isDeleted")
    val isDeleted: Boolean,
    @SerialName("isDisabled")
    val isDisabled: Boolean,
    @SerialName("lastActivity")
    val lastActivity: String,
    @SerialName("notificationSettings")
    val notificationSettings: NotificationSettings,
    @SerialName("reputation")
    val reputation: Int,
    @SerialName("roles")
    val roles: List<String>,
    @SerialName("selectedGenreIds")
    val selectedGenreIds: List<Int>,
    @SerialName("sex")
    val sex: Int,
    @SerialName("status")
    val status: String?,
    @SerialName("userName")
    val userName: String,
    @SerialName("workPreferencesPreset")
    val workPreferencesPreset: String
)