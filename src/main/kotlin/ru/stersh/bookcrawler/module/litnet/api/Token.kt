package ru.stersh.bookcrawler.module.litnet.api


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Token(
    @SerialName("token")
    val token: String
)