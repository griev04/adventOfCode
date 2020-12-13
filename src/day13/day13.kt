package day13

import common.TextFileParser
import kotlin.math.abs

fun main() {
    val data = TextFileParser.parseFile("src/day13/input.txt") { parseData(it) }
    val (departure, busIds) = data
    println("Part 1")
    val result1 = findFirstDepartingBus(departure, busIds)
    println(result1)

    println("Part 2")
    val data2 = TextFileParser.parseFile("src/day13/input.txt") { parseData2(it) }
//    val result2 = resolvePart2BruteForce(data2)
//    println(result2)

    val res = data2.toMutableList()
    outCycle = res[0].first
    while (res.size > 1) {
        val ab = findBetween2(res[0], res[1])
        println(ab)
        res.add(0, ab)
        res.removeAt(1)
        res.removeAt(1)
    }
    val result2 = res[0].first
    println(result2)
}

private fun resolvePart2BruteForce(data2: List<Pair<Long, Int>>): Long {
    var t = data2[0].first
    outerloop@ while (true) {
        loop@ for (step in 1 until data2.size) {
            val (value, stepInList) = data2[step]
            if ((t + stepInList) % value != 0L) {
                break@loop
            }
            if (step == data2.size - 1) {
                break@outerloop
            }
        }
        t += data2[0].first
    }
    return t
}

var outCycle = 0L

fun findBetween2(a: Pair<Long, Int>, b: Pair<Long, Int>): Pair<Long, Int> {
    val offset = abs(a.second - b.second)
    var curr = a.first
    while (true) {
        if ((curr + offset) % b.first == 0L ) {
            outCycle*=b.first
            println(Pair(curr, 0))
            return Pair(curr, 0)
        }
        curr += outCycle
    }
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

fun parseData2(text: String): List<Pair<Long, Int>> {
    return text.split("\n")[1].replace("x", "0").split(",").mapIndexed { index, s -> Pair(s.toLong(), index) }.filter { it.first != 0L }
}
