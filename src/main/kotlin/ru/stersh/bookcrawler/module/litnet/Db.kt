package ru.stersh.bookcrawler.module.litnet

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


object LitnetBookDb : Table() {
    val id = long("id")
    val title = varchar("title", 1024)
    val author = varchar("author", 1024)
    val inSeries = bool("inSeries").default(false)
    val authorId = long("authorId")
    val completed = bool("completed").default(true)
    val read = bool("read").default(false)
    val inLibrary = bool("inLibrary").default(false)
    val purchased = bool("purchased").default(false)
    val coverUrl = varchar("coverPath", 1024).nullable()
    val updatedAt = varchar("updatedAt", 1024)
    val lastModificationTime = long("lastModificationTime")
    override val primaryKey = PrimaryKey(id)
}

data class LitnetBook(
    val id: Long,
    val title: String,
    val author: String,
    val inSeries: Boolean,
    val coverUrl: String?,
    val authorId: Long,
    val completed: Boolean,
    val inLibrary: Boolean,
    val purchased: Boolean,
    val read: Boolean,
    val updatedAt: ZonedDateTime,
    val lastModificationTime: Long,
) {
    companion object {
        fun fromRow(row: ResultRow): LitnetBook {
            return LitnetBook(
                id = row[LitnetBookDb.id],
                title = row[LitnetBookDb.title],
                authorId = row[LitnetBookDb.authorId],
                inSeries = row[LitnetBookDb.inSeries],
                author = row[LitnetBookDb.author],
                completed = row[LitnetBookDb.completed],
                inLibrary = row[LitnetBookDb.inLibrary],
                purchased = row[LitnetBookDb.purchased],
                read = row[LitnetBookDb.read],
                coverUrl = row[LitnetBookDb.coverUrl],
                updatedAt = ZonedDateTime.from(DateTimeFormatter.ISO_ZONED_DATE_TIME.parse(row[LitnetBookDb.updatedAt])),
                lastModificationTime = row[LitnetBookDb.lastModificationTime]
            )
        }
    }
}