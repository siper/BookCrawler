package ru.stersh.bookcrawler.module.litnet.api


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Tag(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String
)