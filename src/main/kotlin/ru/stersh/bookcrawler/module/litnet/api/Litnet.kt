package ru.stersh.bookcrawler.module.litnet.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import ru.stersh.bookcrawler.NETWORK_LOG_LEVEL
import ru.stersh.bookcrawler.Properties
import ru.stersh.bookcrawler.core.Book
import ru.stersh.bookcrawler.core.BookId
import ru.stersh.bookcrawler.module.litnet.LITNET_PROVIDER_NAME
import java.security.MessageDigest
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


object Litnet {
    private const val SECRET = "14a6579a984b3c6abecda6c2dfa83a64"
    private const val APP = "android"

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
            level = NETWORK_LOG_LEVEL
        }
        defaultRequest {
            url("https://api.litnet.com/v1/")
        }
    }

    suspend fun markRead(bookId: Long) {
        client.post("library/move-to") {
            addDefaultParams()
            parameter("book_id", bookId)
            parameter("type", LibInfo.TYPE_ARCHIVE)
        }
    }

    suspend fun addToLibrary(bookId: Long) {
        client.post("library/add") {
            addDefaultParams()
            parameter("book_id", bookId)
            parameter("type", LibInfo.TYPE_WANT_READ)
        }
    }

    suspend fun library(): List<LibraryItem> {
        return client
            .get("library/get") {
                addDefaultParams()
            }
            .body<ArrayList<LibraryItem>>()
    }

    suspend fun getBookDetails(bookId: Long): BookDetails {
        return client
            .get("book/get/${bookId}") {
                addDefaultParams()
            }
            .body<BookDetails>()
    }

    suspend fun getBookChapters(bookId: Long): List<Chapter> {
        return client
            .get("book/contents") {
                addDefaultParams()
                parameter("bookId", bookId)
            }
            .body<ArrayList<Chapter>>()
    }

    suspend fun getChapterTexts(chapterIds: List<Long>): List<ChapterText> {
        return client
            .get("book/get-chapters-texts") {
                addDefaultParams()
                chapterIds.forEach {
                    parameter("chapter_ids[]", it)
                }
            }
            .body<ArrayList<ChapterText>>()
            .map {
                it.copy(text = decrypt(it.text))
            }
    }

    suspend fun getAllSeriesBooks(bookId: Long): List<SeriesBook> {
        return client
            .get("book/details") {
                addDefaultParams()
                parameter("id", bookId)
                parameter("params[]", "series")
            }
            .body<BookDetailsResponse>()
            .series
    }

    suspend fun getBook(bookId: Long): Book {
        val details = getBookDetails(bookId)

        val coverResponse = client.get(details.cover)
        val coverType = Book.Image.Type.fromContentType(coverResponse.headers["Content-Type"])
        val coverBytes = coverResponse.readBytes()

        val cover = Book.Image.Raw(
            bytes = coverBytes,
            type = coverType,
            name = "cover"
        )

        val rawChapters = getBookChapters(bookId).mapNotNull {
            if (it.access) {
                it
            } else {
                null
            }
        }
        val chapterTexts = getChapterTexts(rawChapters.map { it.id })
        val chapters = rawChapters.mapIndexed { index, chapter ->
            Book.Chapter(
                title = chapter.title,
                text = chapterTexts[index].text,
                textType = Book.Chapter.TextType.HTML
            )
        }

        return Book(
            id = BookId(bookId, LITNET_PROVIDER_NAME),
            title = details.title,
            cover = cover,
            annotation = details.annotation,
            authors = listOfNotNull(
                Book.Author(details.authorName), details.coAuthorName?.let { Book.Author(it) }
            ),
            series = null,
            chapters = chapters
        )
    }

    private fun decrypt(text: String): String {
        val encryptedData: ByteArray = Base64
            .getDecoder()
            .decode(text)
        val iv = text
            .toByteArray()
            .take(16)
            .toByteArray()
        val ivSpec = IvParameterSpec(iv)

        val secretKeySpec = SecretKeySpec(SECRET.toByteArray(), "AES")
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec)

        val decryptedText = cipher.doFinal(encryptedData)

        return decryptedText
            .copyOfRange(16, decryptedText.size)
            .toString(Charsets.UTF_8)
            .let { unescapeUnicode(it) }
    }

    private suspend fun login(username: String, password: String): Token {
        return client
            .get("user/find-by-login") {
                addDefaultParams()
                parameter("login", username)
                parameter("password", password)
            }
            .body<Token>()
    }

    private fun HttpRequestBuilder.addDefaultParams() {
        val token = Properties.get("litnet.token")
        val deviceId = getDeviceId()
        parameter("app", APP)
        parameter("device_id", deviceId)
        parameter("sign", createSign(deviceId, token))
        parameter("version", "2023.8.8")
        if (token != null) {
            parameter("user_token", token)
        }
    }

    suspend fun checkAuth() {
        val token = Properties.get("litnet.token")
        if (token != null) {
            return
        }
        val username = Properties.get("litnet.username")
        val password = Properties.get("litnet.password")
        if (username == null || password == null) {
            error("Litnet username or password not set")
        }
        val newToken = login(username, password).token
        Properties.write("litnet.token", newToken)
    }

    private fun getDeviceId(): String {
        val deviceId = Properties.get("litnet.deviceId")
        if (deviceId != null) {
            return deviceId
        }
        val newDeviceId = UUID.randomUUID().toString()
        Properties.write("litnet.deviceId", newDeviceId)
        return newDeviceId
    }

    private fun createSign(deviceId: String, token: String?): String {
        val md = MessageDigest.getInstance("MD5")
        val sign = (deviceId + SECRET + token.orEmpty()).toByteArray(charset = Charsets.US_ASCII)
        val digest = md.digest(sign)
        return digest
            .joinToString(separator = "") { byte ->
                "%02x".format(byte)
            }
            .lowercase()
    }

    private fun unescapeUnicode(source: String): String {
        val result = StringBuilder()
        var i = 0
        while (i < source.length) {
            if (source.length >= i + 6 && source.substring(i, i + 2) == "\\u") {
                result.append(Character.toChars(source.substring(i + 2, i + 6).toInt(16)))
                i += 5
            } else {
                result.append(source[i])
            }
            i++
        }
        return result.toString()
    }
}