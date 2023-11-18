package ru.stersh.bookcrawler.module.at.api


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AccessToken(
    @SerialName("expires")
    val expires: String,
    @SerialName("issued")
    val issued: String,
    @SerialName("token")
    val token: String,
    @SerialName("twoFactorEnabled")
    val twoFactorEnabled: Boolean
)