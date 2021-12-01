package year2021.day01

import common.TextFileParser

fun main() {
    val input = TextFileParser.parseLines("src/year2021/day01/input.txt") { it.toLong() }.toTypedArray()

    println("Day 01 Part 1")
    val result1 = countDepthIncreases(input)
    println(result1)

    println("Day 01 Part 2")
    val result2 = countDepthIncreasesWithSlidingWindow(input, 3)
    println(result2)

}

private fun countDepthIncreases(depthSeries: Array<Long>): Long {
    return countDepthIncreasesWithSlidingWindow(depthSeries)
}

private fun countDepthIncreasesWithSlidingWindow(depthSeries: Array<Long>, windowSize: Int = 1): Long {
    return depthSeries.foldIndexed(0) { index: Int, acc: Long, _ ->
        val isValidRange = index > 0 && index <= depthSeries.size - windowSize
        if ( isValidRange && isDepthIncrease(depthSeries, index, windowSize)) {
            acc+1
        } else {
            acc
        }
    }
}

private fun isDepthIncrease(depthSeries: Array<Long>, index: Int, windowSize: Int = 1) =
    sumValues(depthSeries, index, windowSize) > sumValues(depthSeries, index - 1, windowSize)

private fun sumValues(depthSeries: Array<Long>, currentIndex: Int, windowSize: Int = 1): Long {
    return depthSeries.slice(currentIndex until currentIndex+windowSize).sum()
}