package ru.stersh.bookcrawler.module.at.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import ru.stersh.bookcrawler.Properties
import ru.stersh.bookcrawler.core.Book
import ru.stersh.bookcrawler.core.BookId
import ru.stersh.bookcrawler.module.at.getDecodedText
import java.util.concurrent.atomic.AtomicLong

object At {
    const val PROVIDER_NAME = "at"
    private const val PAGE_SIZE = 10
    private const val ACCESS_TOKEN = "at.accessToken"
    private val lastRequestTime = AtomicLong(System.currentTimeMillis())

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                }
            )
        }
        install(HttpSend)
        install(Logging) {
            level = LogLevel.ALL
        }
        install(Auth) {
            bearer {
                loadTokens {
                    BearerTokens(Properties.require(ACCESS_TOKEN), "")
                }
                refreshTokens {
                    val token = client
                        .post("v1/account/refresh-token")
                        .body<AccessToken>()
                    Properties.write(ACCESS_TOKEN, token.token)
                    BearerTokens(token.token, "")
                }
            }
        }
        defaultRequest {
            url("https://api.author.today/")
        }
    }

    private suspend inline fun <T> delayedRequest(block: () -> T): T {
        val limitTime = lastRequestTime.get() + 1000
        val currentTime = System.currentTimeMillis()
        if (limitTime > currentTime) {
            delay(limitTime - currentTime)
        }
        return block.invoke().also {
            lastRequestTime.set(System.currentTimeMillis())
        }
    }

    suspend fun library(): List<Work> {
        var page = 1
        val libraryResult = mutableListOf<Work>()
        while (true) {
            val library = delayedRequest {
                client
                    .get("v1/account/user-library?page=$page&pageSize=$PAGE_SIZE")
                    .body<LibraryResult>()
            }
            libraryResult.addAll(library.worksInLibrary)
            if (page * PAGE_SIZE >= library.totalCount) {
                break
            }
            page++
        }
        return libraryResult
    }

    suspend fun markRead(workId: Long) {
        delayedRequest {
            client.post("v1/account/update-library-state") {
                contentType(ContentType.Application.Json)
                setBody(LibraryState(LibraryState.Finished, listOf(workId)))
            }
        }
    }

    suspend fun addToLibrary(workId: Long) {
        delayedRequest {
            client.post("v1/account/update-library-state") {
                contentType(ContentType.Application.Json)
                setBody(LibraryState(LibraryState.Saved, listOf(workId)))
            }
        }
    }

    suspend fun getWork(workId: Long): Work {
        return delayedRequest {
            client
                .get("v1/work/$workId/meta-info")
                .body<Work>()
        }
    }

    suspend fun getWorkDetails(workId: Long): WorkDetails {
        return delayedRequest {
            client
                .get("v1/work/$workId/details")
                .body<WorkDetails>()
        }
    }

    suspend fun getChaptersText(workId: Long, chapterIds: List<Long>): List<Chapter> {
        return delayedRequest {
            chapterIds
                .chunked(100)
                .map { chunkedChapterIds ->
                    client.get("v1/work/$workId/chapter/many-texts") {
                        chunkedChapterIds.forEachIndexed { index, chapterId ->
                            parameter("ids[$index]", chapterId.toString())
                        }
                    }.body<List<Chapter>>()
                }
                .flatten()
        }
    }

    suspend fun getCurrentUser(): CurrentUser {
        return delayedRequest {
            client.get("v1/account/current-user").body<CurrentUser>()
        }
    }

    suspend fun getBook(workId: Long): Book {
        val userId = getCurrentUser().id.toString()
        val workDetails = getWorkDetails(workId)

        val chapterIds = workDetails.chapters.map { it.id }
        val chapters = getChaptersText(workId, chapterIds)
            .mapNotNull {
                if (it.key != null && it.text != null) {
                    Book.Chapter(
                        title = it.title,
                        text = getDecodedText(userId, it.key, it.text),
                        textType = Book.Chapter.TextType.HTML
                    )
                } else {
                    null
                }
            }

        val author = Book.Author(workDetails.authorFIO)
        val coAuthor = workDetails.coAuthorFIO?.let { Book.Author(it) }

        val series = if (workDetails.seriesOrder != null && workDetails.seriesTitle != null) {
            Book.Series(
                title = workDetails.seriesTitle,
                order = workDetails.seriesOrder
            )
        } else {
            null
        }

        val coverResponse = client.get(workDetails.coverUrl)

        val coverType = Book.Image.Type.fromContentType(coverResponse.headers["Content-Type"])
        val coverBytes = coverResponse.readBytes()

        val cover = Book.Image.Raw(
            bytes = coverBytes,
            type = coverType,
            name = "cover"
        )

        return Book(
            id = BookId(workId, PROVIDER_NAME),
            cover = cover,
            annotation = workDetails.annotation,
            title = workDetails.title,
            authors = listOfNotNull(author, coAuthor),
            series = series,
            chapters = chapters
        )
    }
}