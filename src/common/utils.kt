package common

import java.io.File

class TextFileParser {
    companion object {
        fun <T> parse(fileName: String, rule: (String) -> T): List<T> {
            return File(fileName).readLines().map { rule(it) }
        }
    }
}