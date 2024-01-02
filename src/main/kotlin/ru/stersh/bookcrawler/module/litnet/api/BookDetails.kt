package ru.stersh.bookcrawler.module.litnet.api


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookDetails(
    @SerialName("adult_only")
    val adultOnly: Boolean,
    @SerialName("allow_comments")
    val allowComments: Boolean,
    @SerialName("annotation")
    val `annotation`: String,
    @SerialName("author_avatar_url")
    val authorAvatarUrl: String,
    @SerialName("author_books_count")
    val authorBooksCount: Int,
    @SerialName("author_id")
    val authorId: Long,
    @SerialName("author_name")
    val authorName: String,
    @SerialName("blocked")
    val blocked: Boolean,
    @SerialName("bonus_balance_promocode")
    val bonusBalancePromocode: Boolean,
    @SerialName("book_active")
    val bookActive: Boolean,
    @SerialName("booktrailer")
    val booktrailer: String?,
    @SerialName("co_author")
    val coAuthor: String?,
    @SerialName("co_author_avatar_url")
    val coAuthorAvatarUrl: String?,
    @SerialName("co_author_books_count")
    val coAuthorBooksCount: Int,
    @SerialName("co_author_name")
    val coAuthorName: String?,
    @SerialName("comments")
    val comments: Int,
    @SerialName("cover")
    val cover: String,
    @SerialName("created_at")
    val createdAt: Int,
    @SerialName("currency_code")
    val currencyCode: String,
    @SerialName("cycle_id")
    val cycleId: Int?,
    @SerialName("cycle_priority")
    val cyclePriority: Int?,
    @SerialName("finished_at")
    val finishedAt: Long?,
    @SerialName("free_chapters")
    val freeChapters: Int,
    @SerialName("genres")
    val genres: List<Genre>,
    @SerialName("has_audio")
    val hasAudio: Boolean?,
    @SerialName("hidden")
    val hidden: Boolean?,
    @SerialName("id")
    val id: Long,
    @SerialName("in_libraries")
    val inLibraries: Int,
    @SerialName("intro")
    val intro: Boolean,
    @SerialName("is_purchased")
    val isPurchased: Boolean,
    @SerialName("lang")
    val lang: String,
    @SerialName("last_update")
    val lastUpdate: Long,
    @SerialName("liked")
    val liked: Boolean,
    @SerialName("likes")
    val likes: Int,
    @SerialName("pages")
    val pages: Int,
    @SerialName("price")
    val price: Float,
    @SerialName("pub_id")
    val pubId: Long?,
    @SerialName("pub_name")
    val pubName: String?,
    @SerialName("publisher")
    val publisher: String?,
    @SerialName("rating")
    val rating: Int,
    @SerialName("rewarded")
    val rewarded: Boolean,
    @SerialName("rewards")
    val rewards: Int,
    @SerialName("selling_frozen")
    val sellingFrozen: Boolean,
    @SerialName("status")
    val status: String,
    @SerialName("tags")
    val tags: List<Tag>,
    @SerialName("text_to_speech_allowed")
    val textToSpeechAllowed: Boolean,
    @SerialName("title")
    val title: String,
    @SerialName("tmp_access_sold")
    val tmpAccessSold: Boolean?,
    @SerialName("total_chr_length")
    val totalChrLength: Int,
    @SerialName("type")
    val type: String,
    @SerialName("url")
    val url: String,
    @SerialName("views")
    val views: Int
)