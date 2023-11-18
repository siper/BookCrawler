package ru.stersh.bookcrawler.core

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.redundent.kotlin.xml.xml

suspend fun Book.toFb2(): String = withContext(Dispatchers.IO) {
    return@withContext xml("FictionBook") {
        xmlns = "http://www.gribuser.ru/xml/fictionbook/2.0"
        namespace("xlink", "http://www.w3.org/1999/xlink")

        "description" {
            "title-info" {
                authors.forEach { author ->
                    "author" {
                        val authorNames = author.name.split(" ")
                        when (authorNames.size) {
                            1 -> {
                                "first-name" {
                                    -authorNames[0]
                                }
                            }
                            2 -> {
                                "first-name" {
                                    -authorNames[0]
                                }
                                "last-name" {
                                    -authorNames[1]
                                }
                            }
                            3 -> {
                                "first-name" {
                                    -authorNames[0]
                                }
                                "middle-name" {
                                    -authorNames[1]
                                }
                                "last-name" {
                                    -authorNames[2]
                                }
                            }
                        }
                    }
                }
                if (cover != null) {
                    "coverpage" {
                        "image" {
                            attribute("xlink:href", "#${cover.name}.${cover.type.extension}")
                        }
                    }
                }
                annotation?.let {
                    "annotation" {
                        unsafeText(clearText(it))
                    }
                }
                "book-title" {
                    "p" {
                        -title
                    }
                }
                series?.let {
                    "sequence" {
                        attribute("name", it.title)
                        attribute("number", it.order)
                    }
                }
            }
        }
        "body" {
            "title" {
                "p" {
                    -title
                }
            }
            chapters.forEach { chapter ->
                "section" {
                    "title" {
                        "p" {
                            -chapter.title
                        }
                    }
                    unsafeText(clearText(chapter.text))
                }
            }
        }

        if (cover != null) {
            "binary" {
                attribute("id", "${cover.name}.${cover.type.extension}")
                attribute("content-type", "image/${cover.type.extension}")
                -cover.toBase64()
            }
        }
    }.toString()
}

private fun clearText(text: String): String {
    var result = text
    TAG_REPLACE.forEach { entry ->
        result = result.replace("<${entry.key}>", "<${entry.value}>")
        result = result.replace("</${entry.key}>", "</${entry.value}>")
    }
    return result
}

private val TAG_REPLACE = mapOf(
    "b" to "strong",
    "i" to "emphasis",
    "em" to "emphasis",
    "del" to "strikethrough",
    "strikethrough" to "strikethrough",
    "blockquote" to "cite",
    "br" to "empty-line/",
)