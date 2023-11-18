package ru.stersh.bookcrawler.module.at

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


object AtBookDb : Table() {
    val id = long("id")
    val title = varchar("title", 1024)
    val author = varchar("author", 1024)
    val series = varchar("series", 1024).nullable()
    val authorId = long("authorId")
    val seriesId = long("seriesId").nullable()
    val completed = bool("completed").default(true)
    val read = bool("read").default(false)
    val inLibrary = bool("inLibrary").default(false)
    val coverUrl = varchar("coverPath", 1024).nullable()
    val updatedAt = varchar("updatedAt", 1024)
    val lastModificationTime = varchar("lastModificationTime", 1024)
    override val primaryKey = PrimaryKey(id)
}

data class AtBook(
    val id: Long,
    val title: String,
    val author: String,
    val series: String?,
    val coverUrl: String?,
    val authorId: Long,
    val seriesId: Long?,
    val completed: Boolean,
    val inLibrary: Boolean,
    val read: Boolean,
    val updatedAt: ZonedDateTime,
    val lastModificationTime: String,
) {
    companion object {
        fun fromRow(row: ResultRow): AtBook {
            return AtBook(
                id = row[AtBookDb.id],
                title = row[AtBookDb.title],
                authorId = row[AtBookDb.authorId],
                seriesId = row[AtBookDb.seriesId],
                series = row[AtBookDb.series],
                author = row[AtBookDb.author],
                completed = row[AtBookDb.completed],
                inLibrary = row[AtBookDb.inLibrary],
                read = row[AtBookDb.read],
                coverUrl = row[AtBookDb.coverUrl],
                updatedAt = ZonedDateTime.from(DateTimeFormatter.ISO_ZONED_DATE_TIME.parse(row[AtBookDb.updatedAt])),
                lastModificationTime = row[AtBookDb.lastModificationTime]
            )
        }
    }
}