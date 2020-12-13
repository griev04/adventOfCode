package day13

import common.TextFileParser

fun main() {
    val data = TextFileParser.parseFile("src/day13/input.txt") { parseData(it) }
    val (departure, busIds) = data
    println("Part 1")
    val result1 = findFirstDepartingBus(departure, busIds)
    println(result1)
}

private fun findFirstDepartingBus(departure: Int, busIds: List<Int>): Int {
    var result = 0
    var min = departure
    busIds.forEach {
        val nextDeparture = it - (departure % it)
        if (nextDeparture < min) {
            min = nextDeparture
            result = it * nextDeparture
        }
    }
    return result
}

fun parseData(text: String): Pair<Int, List<Int>> {
    val (timestamp, busIds) = text.split("\n")
    val validBusIds = busIds.split(",").filter { it != "x" }.map { it.toInt() }
    return Pair(timestamp.toInt(), validBusIds)
}
