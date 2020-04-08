package app.pmsoft.charlist

import app.pmsoft.charlist.counters.KeySet
import app.pmsoft.charlist.counters.TextFileCounter
import java.io.File

fun main() {

    val germanKeySet = KeySet.fromFile(File("src/main/resources/keysets/German.keyset"))
    val counter = TextFileCounter(germanKeySet)
    val nameCount = counter.countName(File("src/main/resources/examples/german/Hänsel und Gretel.txt"))
    val contentCount = counter.countContent(File("src/main/resources/examples/german/Hänsel und Gretel.txt"))

    println("Character statistics for the file name:")
    println(nameCount)
    println("Character statistics for the file content:")
    println(contentCount)
}