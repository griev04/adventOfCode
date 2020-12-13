package day13

import common.TextFileParser
import kotlin.math.abs

fun main() {
    val schedule = TextFileParser.parseFile("src/day13/input.txt") { parseData(it) }

    println("Part 1")
    val result1 = schedule.findFirstDepartingBus()
    println(result1)

    println("Part 2 - UGLY AF")
    val result2 = schedule.findSubsequentBusTimestamp()
    println(result2)
}

class ShuttleSchedule(private val departureTime: Long, private val shuttles: List<Shuttle>) {
    private var subsequentShuttlesCycle = 0L

    fun findSubsequentBusTimestamp(): Long {
        val res = shuttles.toMutableList()
        subsequentShuttlesCycle = res[0].id
        while (res.size > 1) {
            val timestamp = findTimestampOfTwoShuttles(res[0], res[1])
            res.add(0, timestamp)
            res.removeAt(1)
            res.removeAt(1)
        }
        return res[0].id
    }

    private fun findTimestampOfTwoShuttles(a: Shuttle, b: Shuttle): Shuttle {
        val offset = abs(a.offset - b.offset)
        var currentTimestamp = a.id
        while ((currentTimestamp + offset) % b.id != 0L ) {
            currentTimestamp += subsequentShuttlesCycle
        }
        subsequentShuttlesCycle *= b.id
        return Shuttle(currentTimestamp, 0)
    }

    fun findSubsequentBusTimestampBruteForce(): Long {
        var timestamp = shuttles[0].id
        outerloop@ while (true) {
            loop@ for (step in 1 until shuttles.size) {
                val id = shuttles[step].id
                val offset = shuttles[step].offset
                if ((timestamp + offset) % id != 0L) {
                    break@loop
                }
                if (step == shuttles.size - 1) {
                    break@outerloop
                }
            }
            timestamp += shuttles[0].id
        }
        return timestamp
    }

    fun findFirstDepartingBus(): Long {
        var result = 0L
        var min = departureTime
        shuttles.forEach {
            val nextDeparture = it.id - (departureTime % it.id)
            if (nextDeparture < min) {
                min = nextDeparture
                result = it.id * nextDeparture
            }
        }
        return result
    }
}

fun parseData(text: String): ShuttleSchedule {
    val (timestamp, busIds) = text.split("\n")
    val validBusIds = parseBusData(busIds)
    return ShuttleSchedule(timestamp.toLong(), validBusIds)
}

fun parseBusData(text: String): List<Shuttle> {
    return text.replace("x", "0")
            .split(",")
            .mapIndexed { index, s -> Shuttle(s.toLong(), index) }
            .filter { it.id != 0L }
}

class Shuttle(val id: Long, val offset: Int)
