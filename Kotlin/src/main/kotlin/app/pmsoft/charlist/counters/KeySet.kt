package app.pmsoft.charlist.counters

import java.io.File

interface KeySet {

    fun transform(char: Char): String?

    companion object {

        fun fromFile(file: File): KeySet {
            val entries = mutableMapOf<Char, String>()
            for (line in file.readLines(Charsets.UTF_8)) {
                if (line.length == 1) {
                    entries[line[0]] = line[0].toString()
                } else {
                    entries[line[0]] = line.substring(1)
                }
            }
            return KeySetFromFile(entries)
        }

        private class KeySetFromFile(private val transformations: Map<Char, String>) :
            KeySet {

            override fun transform(char: Char): String? {
                return transformations[char] ?: transformations[char.toLowerCase()]
            }
        }
    }
}