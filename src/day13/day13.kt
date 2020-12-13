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

    fun findSubsequentBusTimestamp(): Long {
        val res = shuttles.toMutableList()
        val firstShuttle = res.removeAt(0)
        val equivalentShuttle = findShuttlesEquivalentTimestamp(firstShuttle, res)

        return equivalentShuttle.id
    }

    private fun findShuttlesEquivalentTimestamp(shuttleOne: Shuttle, others: MutableList<Shuttle>, equivalentCycle: Long = shuttleOne.id): Shuttle {
        if (others.size == 0) return shuttleOne

        val shuttleTwo = others.removeAt(0)
        var currentTimestamp = shuttleOne.id
        val offset = abs(shuttleOne.offset - shuttleTwo.offset)
        while ((currentTimestamp + offset) % shuttleTwo.id != 0L ) {
            currentTimestamp += equivalentCycle
        }

        val equivalentShuttle = Shuttle(currentTimestamp, shuttleOne.offset)
        return findShuttlesEquivalentTimestamp(equivalentShuttle, others, equivalentCycle * shuttleTwo.id)
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
