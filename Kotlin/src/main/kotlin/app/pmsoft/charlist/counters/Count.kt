package app.pmsoft.charlist.counters

interface Count {

    enum class Type {
        CONTENT, NAME
    }

    val type: Type

    val fileExtension: String

    val singles: Map<Char, Int>
    val bigrams: Map<String, Int>
    val trigrams: Map<String, Int>

    val totalCharacters: Int
    val totalBigrams: Int
    val totalTrigrams: Int
}