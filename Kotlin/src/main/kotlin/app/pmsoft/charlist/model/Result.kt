package app.pmsoft.charlist.model

import app.pmsoft.charlist.counters.Count

data class Result(
    val count: Count,
    val contentType: ContentType,
    val language: String,
    val keySet: String
) {
}