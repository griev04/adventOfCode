package year2019.day03

import common.TextFileParser
import kotlin.math.abs

fun main() {
    val wireDirections = TextFileParser.parseLines("src/year2019/day03/input.txt") { parseDisplacement(it) }
    val panel = Panel().moveAlongPaths(wireDirections)

    println("Part 1")
    val result1 = panel.getClosestIntersection()
    println(result1)

    println("Part 2")
    val result2 = panel.getMinimumCableDistanceIntersection()
    println(result2)
}

class Panel {
    private val origin = Position(0, 0)
    private lateinit var wirePaths: List<List<Position>>
    private lateinit var intersections: Set<Position>

    fun moveAlongPaths(wireDirections: List<List<PathDirection>>): Panel {
        wirePaths = wireDirections.map { displacements ->
            var currentPosition = origin
            val pathsPoints = mutableListOf(origin)
            displacements.forEach { displacement ->
                val direction = displacement.getDirection()
                for (module in 1..displacement.distance) {
                    val newPosition = direction + currentPosition
                    pathsPoints.add(newPosition)
                    currentPosition = newPosition
                }
            }
            pathsPoints
        }
        intersections = wirePaths[0].intersect(wirePaths[1]).minus(origin)
        return this
    }

    fun getClosestIntersection(): Int? {
        return intersections.map { abs(it.x) + abs(it.y) }.min()
    }

    fun getMinimumCableDistanceIntersection(): Int? {
        return intersections.map { intersection ->
            wirePaths[0].indexOf(intersection) + wirePaths[1].indexOf(intersection)
        }.min()
    }
}

fun parseDisplacement(value: String): List<PathDirection> {
    return value.split(",").map {
        val direction = it.first()
        val distance = it.substring(1, it.length).toInt()
        PathDirection(direction, distance)
    }
}

class Position(val x: Int, val y: Int) {
    operator fun plus(other: Position): Position {
        return Position(this.x + other.x, this.y + other.y)
    }

    override fun equals(other: Any?): Boolean {
        if (other is Position) {
            return this.x == other.x && this.y == other.y
        }
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return "$x,$y".hashCode()
    }
}

class PathDirection(private val direction: Char, val distance: Int) {
    fun getDirection(): Position {
        return when (direction) {
            'U' -> Position(0, 1)
            'D' -> Position(0, -1)
            'R' -> Position(1, 0)
            'L' -> Position(-1, 0)
            else -> Position(0, 0)
        }
    }
}
