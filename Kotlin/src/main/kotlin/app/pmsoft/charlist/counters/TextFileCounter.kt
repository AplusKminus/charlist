package app.pmsoft.charlist.counters

import java.io.File
import java.io.InputStreamReader
import java.io.Reader
import java.io.StringReader
import java.util.*
import kotlin.collections.HashMap

open class TextFileCounter(private val alphabet: Alphabet) : Counter {

    private var nMinus2 = ' '
    private var nMinus1 = ' '
    private var char = ' '
    private var charBuffer = CharArray(1024) { 0.toChar() }

    private val readQueue = ArrayDeque<Char>()

    private val singles: MutableMap<Char, Int> = mutableMapOf()
    private val bigrams: MutableMap<String, Int> = mutableMapOf()
    private val trigrams: MutableMap<String, Int> = mutableMapOf()

    open val nGramFilters: List<(String) -> Boolean> = listOf(
        { it -> ' ' in it },
        { it -> '\n' in it },
        { it -> '\r' in it },
        { it -> '\t' in it }
    )

    private fun putSingle(char: Char) {
        singles[char] = singles.getOrDefault(char, 0) + 1
    }

    private fun putBigram(bigram: String) {
        if (nGramFilters.none { it(bigram) }) {
            bigrams[bigram] = bigrams.getOrDefault(bigram, 0) + 1
        }
    }

    private fun putTrigram(trigram: String) {
        if (nGramFilters.none { it(trigram) }) {
            trigrams[trigram] = trigrams.getOrDefault(trigram, 0) + 1
        }
    }

    private fun advanceQueue() {
        nMinus2 = nMinus1
        nMinus1 = char
        char = readQueue.removeFirst()
    }

    override fun countContent(file: File): Count {
        synchronized(this) {
            count(InputStreamReader(file.inputStream(), Charsets.UTF_8))
            return SimpleCount(
                HashMap(singles),
                HashMap(bigrams),
                HashMap(trigrams)
            )
        }
    }

    override fun countName(file: File): Count {
        synchronized(this) {
            count(StringReader(file.name))
            return SimpleCount(
                HashMap(singles),
                HashMap(bigrams),
                HashMap(trigrams)
            )
        }
    }

    private fun count(reader: Reader) {
        reader.read(charBuffer, 0, 3)
        for (rawChar in charBuffer) {
            readQueue.addAll(
                handleMissingChar(
                    alphabet.transform(rawChar)
                )
            )
        }
        if (readQueue.size < 3) {
            for (char in readQueue) {
                putSingle(char)
            }
            // readQueue.size == 0 does not matter
            // readQueue.size == 1 already covered, no bigram possible
            if (readQueue.size == 2) {
                val bigram = readQueue.joinToString()
                putBigram(bigram)
            }
            return
        }
        // first char
        advanceQueue()
        putSingle(char)
        // second char
        advanceQueue()
        putSingle(char)
        putBigram(nMinus1.toString() + char)
        // third char
        advanceQueue()
        putSingle(char)
        putBigram(nMinus1.toString() + char)
        putTrigram(nMinus2.toString() + nMinus1 + char)
        while (reader.ready()) {
            if (reader.read(charBuffer) <= 0) {
                break
            }
            for (rawChar in charBuffer) {
                readQueue.addAll(
                    handleMissingChar(
                        alphabet.transform(rawChar)
                    )
                )
            }
            while (!readQueue.isEmpty()) {
                advanceQueue()
                putSingle(char)
                putBigram(nMinus1.toString() + char)
                putTrigram(nMinus2.toString() + nMinus1 + char)
            }
        }
    }

    private fun handleMissingChar(input: String?): Iterable<Char> {
        if (input != null) {
            return input.asIterable()
        }
        return "".asIterable()
    }
}