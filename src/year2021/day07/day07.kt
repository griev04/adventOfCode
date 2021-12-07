package year2021.day07

import common.TextFileParser
import kotlin.math.abs

fun main() {
    val submarineFleet = TextFileParser.parseFile("src/year2021/day07/input.txt") { data ->
        SubmarineFleet.make(data.removeSuffix("\n").split(",").map { it.toInt() })
    }

    println("Day 07 Part 1")
    val result1 = submarineFleet.getMinimumFuel()
    println(result1)

    println("Day 07 Part 2")
    val result2 = submarineFleet.getAlternativeMinimumFuel()
    println(result2)
}

class SubmarineFleet private constructor(private val initialPositions: List<Int>) {
    fun getMinimumFuel(): Long? {
        return getMinimum()
    }

    fun getAlternativeMinimumFuel(): Long? {
        return getMinimum(true)
    }

    private fun getMinimum(boolean: Boolean = false): Long? {
        val minPosition = initialPositions.min()
        val maxPosition = initialPositions.max()

        if (minPosition == null || maxPosition == null) return null

        var minimum = computeAllFuel(minPosition, boolean)

        val results = mutableMapOf<Int, Long>()

        (minPosition..maxPosition).forEach { target ->
            var current = 0L
            for (position in initialPositions) {
                if (current > minimum) {
                    break
                }
                val increase = results.getOrDefault(position - target, computeFuelFor(position, target, boolean))
                results[position - target] = increase
                current += increase
            }
            if (current < minimum) minimum = current
        }
        return minimum
    }

    private fun computeAllFuel(target: Int, boolean: Boolean): Long {
        return initialPositions.fold(0L) { acc, i ->
            acc + computeFuelFor(i, target, boolean)
        }
    }

    private fun computeFuelFor(position: Int, target: Int, boolean: Boolean): Long {
        return if (boolean) {
            factorialSum(abs(position - target).toLong())
        } else {
            abs(position - target).toLong()
        }
    }

    private fun factorialSum(n: Long): Long {
        if (n == 0L) return 0
        return n + factorialSum(n - 1)
    }

    companion object {
        fun make(initialPositions: List<Int>): SubmarineFleet {
            return SubmarineFleet(initialPositions.sorted())
        }
    }
}
