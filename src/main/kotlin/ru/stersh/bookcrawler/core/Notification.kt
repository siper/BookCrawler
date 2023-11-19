package ru.stersh.bookcrawler.core

data class Notification(
    val id: BookId,
    val type: MessageType,
    val title: String,
    val coverUrl: String,
    val author: String,
    val series: Series?,
    val availableActions: List<Action>
) {
    data class Series(val title: String, val order: Int)
}