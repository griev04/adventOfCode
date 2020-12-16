package year2020.day15

import common.TextFileParser

fun main() {
    val sequence = TextFileParser.parseFile("src/year2020.day15/input.txt") { parse(it) }

    val res1 = playMemoryGame(30000000, sequence)
    println(res1)
}

fun playMemoryGame(turns: Int, seed: List<Int>): Int {
    val seen = seed.take(seed.size - 1).mapIndexed { index, el -> el to index + 1 }.toMap().toMutableMap()
    var previous: Int
    var lastSpoken = seed.last()

    for (turn in seed.size + 1..turns) {
        previous = lastSpoken
        lastSpoken = if (seen[lastSpoken] == null) {
            0
        } else {
            turn - 1 - seen[lastSpoken]!!
        }
        seen[previous] = turn - 1
    }
    return lastSpoken
}

fun parse(numbers: String): List<Int> {
    return numbers.split("\n")[0].split(",").map { it.toInt() }
}
