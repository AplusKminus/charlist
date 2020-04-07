package app.pmsoft.charlist.counters

data class SimpleCount(
    override val type: Count.Type,
    override val fileExtension: String,
    override val singles: Map<Char, Int>,
    override val bigrams: Map<String, Int>,
    override val trigrams: Map<String, Int>
) : Count {

    override fun toString(): String {
        val builder = StringBuilder()
            .append("Character statistics for ")
            .append(typeString)
            .append(" of ")
            .append(fileExtension)
            .append(" files:\n")
        val headLineLength = builder.length - 1
        for (i in 1..headLineLength) {
            builder.append('=')
        }
        builder.append('\n')
        builder.append("Single characters (")
        builder.append(totalTrigrams)
        builder.append("):\n")
        singles.entries.sortedBy(Map.Entry<Char, Int>::value).reversed().forEach {
            builder.append('\t')
            builder.append(
                when (it.key) {
                    ' ' -> "SPC"
                    '\t' -> "TAB"
                    '\n' -> "LF"
                    '\r' -> "CR"
                    else -> it.key
                }
            )
            builder.append('\t')
            builder.append(String.format("%5d\t%5.2f%%\n", it.value, it.value * 100.0 / totalCharacters))
        }
        if (totalBigrams > 0) {
            builder.append("\nBigrams (")
            appendNGramStats(builder, bigrams, totalBigrams)
        }
        if (totalTrigrams > 0) {
            builder.append("\nTrigrams (")
            appendNGramStats(builder, trigrams, totalTrigrams)
        }
        return builder.toString()
    }

    private fun appendNGramStats(
        builder: java.lang.StringBuilder,
        nGrams: Map<String, Int>,
        total: Int
    ) {
        builder.append(total)
        builder.append(" total, ")
        builder.append(nGrams.size)
        builder.append(" unique, showing only > 1%):\n")
        nGrams.entries.filter { it.value * 100.0 / total >= 1 }.sortedBy(Map.Entry<String, Int>::value).reversed()
            .forEach {
                builder.append('\t')
                builder.append(it.key)
                builder.append('\t')
                builder.append(String.format("%5d\t%5.2f%%\n", it.value, it.value * 100.0 / total))
            }
    }

    override val totalCharacters = singles.values.sum()
    override val totalBigrams = bigrams.values.sum()
    override val totalTrigrams = trigrams.values.sum()

    private val typeString = when (type) {
        Count.Type.CONTENT -> "file contents"
        Count.Type.NAME -> "file name"
    }
}