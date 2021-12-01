package year2021.day01

import common.TextFileParser

fun main() {
    val input = TextFileParser.parseLines("src/year2021/day01/input.txt") { it.toLong() }.toTypedArray()

    println("Day 01 Part 1")
    val result1 = countDepthIncreases(input)
    println(result1)

    println("Day 01 Part 2")
    val result2 = countGroupedDepthIncreases(input)
    println(result2)

}

fun countGroupedDepthIncreases(depthSeries: Array<Long>): Int {
    return countDepthIncreases(depthSeries, 3)
}

fun countDepthIncreases(depthSeries: Array<Long>, windowSize: Int = 1): Int {
    var count = 0
    (windowSize until depthSeries.size).forEach { index ->
        if (isDepthIncrease(depthSeries, index, windowSize)) count++
    }
    return count
}

private fun isDepthIncrease(depthSeries: Array<Long>, index: Int, windowSize: Int): Boolean =
    sumSlidingWindowValues(depthSeries, index, windowSize, 1) > sumSlidingWindowValues(depthSeries, index, windowSize)

private fun sumSlidingWindowValues(depthSeries: Array<Long>, currentIndex: Int, windowSize: Int = 1, offset: Int = 0): Long =
    depthSeries.slice(currentIndex-windowSize+offset until currentIndex+offset).sum()
