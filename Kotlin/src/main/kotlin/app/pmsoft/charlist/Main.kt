package app.pmsoft.charlist

import app.pmsoft.charlist.counters.Alphabet
import app.pmsoft.charlist.counters.TextFileCounter
import java.io.File

fun main() {

    val germanAlphabet = Alphabet.fromFile(File("src/main/resources/alphabets/German.alphabet"))
    val counter = TextFileCounter(germanAlphabet)
    val nameCount = counter.countName(File("src/main/resources/examples/german/Hänsel und Gretel.txt"))
    val contentCount = counter.countContent(File("src/main/resources/examples/german/Hänsel und Gretel.txt"))

    println("Character statistics for the file name:")
    println(nameCount)
    println("Character statistics for the file content:")
    println(contentCount)
}