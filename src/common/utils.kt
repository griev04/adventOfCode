package common

import java.io.File

fun readTextFile(fileName: String): List<String> {
    return File(fileName).readLines()
}