package year2020.day15

import common.TextFileParser

fun main() {
    val sequence = TextFileParser.parseFile("src/year2020/day15/input.txt") { parse(it) }

    println("Day 15 Part 1")
    val res1 = playMemoryGame(2020, sequence)
    println(res1)

    println("Day 15 Part 2")
    val res2 = playMemoryGame(30000000, sequence)
    println(res2)
}

fun playMemoryGame(turns: Int, seed: List<Int>): Int {
    val seen = IntArray(turns) { 0 }
    seed.take(seed.size - 1).forEachIndexed { index, el ->
        seen[el] = index + 1
    }
    var previous: Int
    var lastSpoken = seed.last()

    for (turn in seed.size + 1..turns) {
        previous = lastSpoken
        lastSpoken = if (seen[lastSpoken] == 0) {
            0
        } else {
            turn - 1 - seen[lastSpoken]
        }
        seen[previous] = turn - 1
    }
    return lastSpoken
}

fun parse(numbers: String): List<Int> {
    return numbers.split("\n")[0].split(",").map { it.toInt() }
}
