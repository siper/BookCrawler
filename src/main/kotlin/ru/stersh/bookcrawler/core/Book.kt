package ru.stersh.bookcrawler.core

data class Book(
    val id: BookId,
    val title: String,
    val cover: Image?,
    val annotation: String?,
    val authors: List<Author>,
    val series: Series?,
    val chapters: List<Chapter>
) {
    data class Author(val name: String)
    data class Series(
        val title: String,
        val order: Int
    )

    data class Chapter(
        val title: String,
        val text: String,
        val textType: TextType
    ) {
        enum class TextType { HTML }
    }

    sealed interface Image {
        val type: Type
        val name: String

        class Raw(
            val bytes: ByteArray,
            override val name: String,
            override val type: Type
        ) : Image
//        data class Base64(val base64data: String, val type: Type) : Image

        enum class Type {
            PNG, JPG;

            val extension: String
                get() {
                    return when (this) {
                        Type.PNG -> "png"
                        Type.JPG -> "jpg"
                    }
                }

            companion object {
                fun fromContentType(contentType: String?): Type {
                    return when(contentType) {
                        "image/jpeg" -> JPG
                        "image/png" -> PNG
                        else -> error("Unknown content type: $contentType")
                    }
                }
            }
        }
    }
}