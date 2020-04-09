package app.pmsoft.charlist.persistence

import app.pmsoft.charlist.model.Result
import java.io.File
import java.io.FileOutputStream

class ResultToCSV(private val comma: String = "\t", private val newLine: String = "\n") {

    fun toCSV(result: Result, file: File) {
        FileOutputStream(file).writer().use { writer ->
            writer.append("Content$comma Language$comma Characters $comma Bigrams $comma Trigrams$newLine")
            writer.append(
                "${result.contentType}$comma" +
                    "${result.language}$comma" +
                    "${result.count.totalCharacters}$comma" +
                    "${result.count.totalBigrams}$comma" +
                    "${result.count.totalTrigrams}$newLine"
            )
            writer.append("Key$comma Count$comma Ratio$newLine")
            result.count.singles.entries.sortedBy { it.value }.reversed().forEach {
                writer.append(escape(it.key.toString())).append(comma)
                writer.append(it.value.toString()).append(comma)
                writer.append(String.format("\"%6.4f\"", it.value.toDouble() / result.count.totalCharacters))
                    .append(newLine)
            }
            writer.append("Bigram$comma Count$comma Ratio$newLine")
            result.count.bigrams.entries.sortedBy { it.value }.reversed().forEach {
                writer.append(escape(it.key)).append(comma)
                writer.append(it.value.toString()).append(comma)
                writer.append(String.format("\"%6.4f\"", it.value.toDouble() / result.count.totalBigrams))
                    .append(newLine)
            }
            writer.append("Trigram$comma Count$comma Ratio$newLine")
            result.count.trigrams.entries.sortedBy { it.value }.reversed().forEach {
                writer.append(escape(it.key)).append(comma)
                writer.append(it.value.toString()).append(comma)
                writer.append(String.format("\"%6.4f\"", it.value.toDouble() / result.count.totalTrigrams))
                    .append(newLine)
            }
        }
    }

    private fun escape(string: String): String {
        var input = string
            .toCharArray()
            .joinToString(separator = "", transform = ::charToPrintable)
            .replace(Regex.fromLiteral("\""), "\"\"")
        if (comma in input || newLine in input || '"' in input) {
            input = "\"$input\""
        }
        return input
    }

    private fun charToPrintable(char: Char): String {
        return when (char) {
            ' ' -> "[SPACE]"
            '\t' -> "[TAB]"
            '\n' -> "[LF]"
            '\r' -> "[CR]"
            else -> char.toString()
        }
    }
}