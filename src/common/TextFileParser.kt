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

        fun <T> parseAsIndexedMap(fileName: String, transform: (String) -> T): Map<Int, T> {
            val lines = File(fileName).readLines()
            val map = mutableMapOf<Int, T>()
            lines.forEachIndexed { index, line ->
                map[index] = transform(line)
            }
            return map
        }

        fun getSizeOfGrid(fileName: String): Pair<Int, Int> {
            val grid = File(fileName).readLines()
            var maxLineLength = 0
            grid.forEach { line ->
                if (maxLineLength < line.length) {
                    maxLineLength = line.length
                }
            }
            return Pair(maxLineLength, grid.size)
        }
    }
}