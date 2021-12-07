package year2021.day06

import common.TextFileParser

fun main() {
    val input = TextFileParser.parseFile("src/year2021/day06/input.txt") { data ->
        data.removeSuffix("\n").split(",").map { it.toInt() }
    }
    println("Day06 Part 1")
    val result1 =  PopulationTracker().setInitialPopulation(input).playDays(80).getPopulationSize()
    println(result1)

    println("Day06 Part 2")
    val result2 = PopulationTracker().setInitialPopulation(input).playDays(256).getPopulationSize()
    println(result2)
}

class PopulationTracker {
    private var populationCountByTimer = LongArray(CHILDHOOD_LENGTH + 1) { 0 }

    fun setInitialPopulation(timers: List<Int>): PopulationTracker {
        timers.groupBy { it }.forEach { (timer, timers) ->
            populationCountByTimer[timer] = timers.size.toLong()

        }
        return this
    }

    fun getPopulationSize(): Long {
        return populationCountByTimer.sum()
    }

    fun playDays(days: Int): PopulationTracker {
        repeat(days) {
            playDay()
        }
        return this
    }

    private fun playDay(): PopulationTracker {
        val new = LongArray(CHILDHOOD_LENGTH + 1)
        (CHILDHOOD_LENGTH downTo 0).forEach { timer ->
            if (timer == 0) {
                new[REPRODUCTION_INTERVAL] = new[REPRODUCTION_INTERVAL] + populationCountByTimer[timer]
                new[CHILDHOOD_LENGTH] = new[CHILDHOOD_LENGTH] + populationCountByTimer[timer]
            } else {
                new[timer - 1] = populationCountByTimer[timer]
            }
        }
        populationCountByTimer = new
        return this
    }

    companion object {
        const val REPRODUCTION_INTERVAL = 6
        const val CHILDHOOD_LENGTH = 8
    }

}
