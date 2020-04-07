package app.pmsoft.charlist.counters

interface Count {

    val singles: Map<Char, Int>
    val bigrams: Map<String, Int>
    val trigrams: Map<String, Int>

    val totalCharacters: Int
    val totalBigrams: Int
    val totalTrigrams: Int
}