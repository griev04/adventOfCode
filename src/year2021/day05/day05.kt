package year2021.day05

import common.TextFileParser
import kotlin.math.abs

fun main() {
    val input = TextFileParser.parseLines("src/year2021/day05/input.txt") { Segment.makeFromReading(it) }

    println("Day 05 Part 1")
    val result1 = Navigator(input).getDangerousPoints()
    println(result1)

    println("Day 05 Part 2")
    val result2 = Navigator(input).getAllDangerousPoints()
    println(result2)
}

class Navigator(private val clouds: List<Segment>) {
    fun getDangerousPoints(): Int {
        return getDangerousPointsCount()
    }

    fun getAllDangerousPoints(): Int {
        return getDangerousPointsCount(false)
    }

    private fun getDangerousPointsCount(onlyMainDirections: Boolean = true): Int {
        val map = mutableMapOf<Position, Int>()

        val visibleClouds = if (onlyMainDirections) {
            clouds.filter { it.isHorizontalOrVertical() }
        } else {
            clouds
        }

        return visibleClouds.fold(map) { acc, cloud ->
            val points = cloud.getPoints()
            points.forEach { p ->
                map[p] = acc.getOrDefault(p, 0) + 1
            }
            acc
        }.count { it.value > 1 }
    }
}

class Segment(private val start: Position, private val end: Position) {
    fun isHorizontalOrVertical(): Boolean {
        return isVerticalLine() || isHorizontalLine()
    }

    override fun toString(): String {
        return "$start -> $end"
    }

    private fun isHorizontalLine() = start.y == end.y

    private fun isVerticalLine() = start.x == end.x

    fun getPoints(): List<Position> {
        val stepX = if(start.x != end.x) {
            (end.x - start.x) / abs(end.x - start.x)
        } else 0
        val stepY = if(start.y != end.y) {
            (end.y - start.y) / abs(end.y - start.y)
        } else 0
        val positions = mutableListOf<Position>()
        var currentPoint = start
        while (currentPoint.x != end.x || currentPoint.y != end.y) {
            positions.add(currentPoint)
            currentPoint = Position(currentPoint.x + stepX, currentPoint.y + stepY)
        }
        positions.add(end)
        return positions
    }

    companion object {
        fun makeFromReading(rawValues: String): Segment {
            val positions = rawValues.trim().split("->").map {
                val coordinates = it.trim().split(",").map { c -> c.toInt() }
                Position(coordinates[0], coordinates[1])
            }
            return Segment(positions[0], positions[1])
        }
    }

}

class Position(val x: Int, val y: Int) {
    override fun equals(other: Any?): Boolean {
        if (other is Position) {
            return this.x == other.x && this.y == other.y
        }
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return "$x,$y".hashCode()
    }

    override fun toString(): String {
        return "$x; $y"
    }
}
