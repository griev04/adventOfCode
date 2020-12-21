package year2019.day05

import common.TextFileParser

fun main() {
    val program = TextFileParser.parseFile("src/year2019/day05/input.txt") { text ->
        Program(text.replace("\n", "").split(",").map { it.toInt() })
    }
    program.run()
}
