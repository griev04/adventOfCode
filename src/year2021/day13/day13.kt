package year2021.day13

import common.TextFileParser

fun main() {
    val paper = TextFileParser.parseFile("src/year2021/day13/input.txt") { parsePaper(it) }

    println("Day 13 Part 1")
    val result1 = paper.getPointsAfterFirstFold()
    println(result1)

    println("Day 13 Part 2")
    val result2 = paper.toString()
    println(result2)
}

fun parsePaper(file: String): Paper {
    val (points, folds) = file.split("\n\n").map { it.split("\n") }
    return Paper.make(points, folds)
}

class Paper(inputPoints: Set<Pair<Int, Int>>, foldInstructions: List<Pair<String, Int>>) {
    private val points: MutableSet<Pair<Int, Int>>
    private var pointsAfterFirstFold: Int = inputPoints.size

    init {
        points = inputPoints.toMutableSet()
        foldWith(foldInstructions)
    }

    fun getPointsAfterFirstFold(): Int {
        return pointsAfterFirstFold
    }

    private fun foldWith(foldInstructions: List<Pair<String, Int>>) {
        foldInstructions.forEachIndexed { index, instruction ->
            foldOnceWith(instruction)
            if (index == 0) {
                pointsAfterFirstFold = points.size
            }
        }
    }

    private fun foldOnceWith(instruction: Pair<String, Int>) {
        val (direction, value) = instruction
        val targetPoints = findPointsToFold(direction, value)
        val newPoints = getNewPoints(targetPoints, direction, value)

        points.removeAll(targetPoints)
        points.addAll(newPoints)
    }

    private fun findPointsToFold(
        direction: String,
        value: Int
    ) = points.filter { point ->
        if (direction == "y") {
            point.second >= value
        } else {
            point.first >= value
        }
    }.toSet()

    private fun getNewPoints(
        targetPoints: Set<Pair<Int, Int>>,
        direction: String,
        value: Int
    ) = targetPoints.mapNotNull { targetPoint ->
        if (direction == "x" && targetPoint.first != value) {
            Pair(value - (targetPoint.first - value), targetPoint.second)
        } else if (targetPoint.second != value) {
            Pair(targetPoint.first, value - (targetPoint.second - value))
        } else null
    }

    override fun toString(): String {
        val width = points.maxBy { it.first }?.first ?: 0
        val height = points.maxBy { it.second }?.second ?: 0

        val grid: Array<CharArray> = Array(height + 1) { CharArray(width + 1) { ' ' } }
        points.forEach { grid[it.second][it.first] = '#' }

        return grid.joinToString("\n") { it.joinToString(" ") }
    }

    companion object {
        fun make(points: List<String>, folds: List<String>): Paper {

            val coordinates = points.map {
                val values = it.split(",").map { c -> c.toInt() }
                Pair(values.first(), values.last())
            }.toSet()

            val foldInstructions = folds.map { f ->
                val values = f.removePrefix("fold along ").split("=")
                Pair(values.first(), values.last().toInt())
            }

            return Paper(coordinates, foldInstructions)
        }
    }
}
