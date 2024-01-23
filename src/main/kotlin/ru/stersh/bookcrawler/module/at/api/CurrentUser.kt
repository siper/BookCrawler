package ru.stersh.bookcrawler.module.at.api


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrentUser(
    @SerialName("id")
    val id: Int
)