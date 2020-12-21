package common

import java.io.File

class TextFileParser {
    companion object {
        fun <T> parseLines(fileName: String, transform: (String) -> T): List<T> {
            return File(fileName).readLines().map(transform)
        }

        fun <T> parseFile(fileName: String, transform: (String) -> T): T {
            val text = File(fileName).readText()
            return transform(text)
        }

        fun <T> parseGroupedData(fileName: String, groupSeparator: String = "\n\n", transform: (List<String>) -> T): List<T> {
            val text = File(fileName).readText()
            return text.split(groupSeparator).map { group ->
                val groupValues = group.replace("\n", " ").split(" ").filter { it.isNotEmpty() }
                transform(groupValues)
            }
        }
    }
}