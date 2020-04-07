package app.pmsoft.charlist.counters

import java.io.File

interface Counter {

    fun countContent(file: File): Count

    fun countName(file: File): Count
}