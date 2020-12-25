package year2020.day03

import common.TextFileParser

fun main() {
    val inputFile = "src/year2020/day03/input.txt"
    val input = TextFileParser.parseLines(inputFile) { it }

    println("Day 03 Part 1")
    val result1 = move(input, 3, 1)
    println(result1)

    println("Day 03 Part 2")
    val slopes = listOf(Pair(1, 1), Pair(3, 1), Pair(5, 1), Pair(7, 1), Pair(1, 2))
    val result2 = slopes.fold(1L) { acc, navigationPattern ->
        val numberOfTreesOfSlope = move(input, navigationPattern.first, navigationPattern.second)
        acc * numberOfTreesOfSlope
    }
    println(result2)
}

fun move(pattern: List<String>, dx: Int, dy: Int): Long {
    var column = 0
    var counter = 0
    for (row in pattern.indices step dy) {
        if (pattern[row][column] == "#".first()) {
            counter++
        }
        column = (column + dx) % pattern[0].length
    }
    return counter.toLong()
}
