package year2019.day04

import common.TextFileParser

fun main() {
    val passwordRange = TextFileParser.parseFile("src/year2019/day04/input.txt") { line ->
        val boundaries = line.split("-").map { it.toInt() }
        IntRange(boundaries[0], boundaries[1])
    }
    println("Part 1")
    println(checkPasswords(passwordRange, false))

    println("Part 2")
    println(checkPasswords(passwordRange, true))
}

fun checkPasswords(valuesRange: IntRange, onlyTwoAdjacent: Boolean = false): Int {
    var count = 0
    valuesRange.forEach{ num ->
        val hasAdjacentDigits = hasAdjacentDigits(num, onlyTwoAdjacent)
        if (hasAdjacentDigits && checkForIncreasingSequence(num.toString())) {
            count++
        }
    }
    return count
}

private fun hasAdjacentDigits(num: Int, onlyTwoAdjacent: Boolean = false): Boolean {
    val digitsDistribution = num.toString().groupBy { it }
    val digitsToCheck = digitsDistribution.filterValues { it.size > 1 }
    var hasAdjacentDigits = false

    for (digitToCheck in digitsToCheck) {
        val (digit, occurrence) = digitToCheck
        var isAdjacent = num.toString().indexOf("$digit$digit") != -1
        if (onlyTwoAdjacent && isAdjacent && occurrence.size > 2) {
            isAdjacent = num.toString().indexOf("$digit$digit$digit") == -1
        }
        if (isAdjacent) {
            hasAdjacentDigits = true
        }
    }
    return hasAdjacentDigits
}

fun checkForIncreasingSequence(string: String, previous: Int = 0): Boolean {
    if (string.isEmpty()) return true
    if (string[0].toInt() < previous) return false
    if (checkForIncreasingSequence(string.removeRange(0, 1), string[0].toInt())) {
        return true
    }
    return false
}