package com.firdavs.persianliterature.author.ui.work_details

import org.json.JSONObject
import java.io.File

/**
 * Parsed representation of a work's OCR-extracted text, downloaded as JSON from
 * Firebase Storage. Structure mirrors the converter output:
 *
 * ```
 * { "title": "...", "pageCount": 170,
 *   "pages": [ { "page": 21, "lines": ["...", "..."] } ] }
 * ```
 */
data class WorkText(
    val title: String,
    val pages: List<Page>
) {
    data class Page(
        val page: Int,
        val lines: List<String>
    )

    companion object {
        fun fromJson(json: String): WorkText {
            val root = JSONObject(json)
            val pagesArray = root.optJSONArray("pages")
            val pages = buildList {
                if (pagesArray != null) {
                    for (i in 0 until pagesArray.length()) {
                        val pageObj = pagesArray.getJSONObject(i)
                        val linesArray = pageObj.optJSONArray("lines")
                        val lines = buildList {
                            if (linesArray != null) {
                                for (j in 0 until linesArray.length()) {
                                    add(linesArray.getString(j))
                                }
                            }
                        }
                        add(Page(page = pageObj.optInt("page", i + 1), lines = lines))
                    }
                }
            }
            return WorkText(title = root.optString("title", ""), pages = pages)
        }

        fun fromFile(file: File): WorkText =
            fromJson(file.readText(Charsets.UTF_8))
    }
}
