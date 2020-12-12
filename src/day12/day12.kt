package day12

import common.TextFileParser
import kotlin.math.abs

fun main() {
    val instructions = TextFileParser.parseLines("src/day12/input.txt") { Pair(it[0], it.takeLast(it.length - 1).toInt()) }
    println("Part 1")
    println(findDistance(instructions))
}

fun findDistance(instructions: List<Pair<Char, Int>>): Int {
    var currentDirection = 'E'
    var dLong = 0
    var dLat = 0

    instructions.forEach { instruction ->
        val (dir, value) = instruction
        when (dir) {
            'F' -> {
                val (lat, long) = Direction.getDisplacement(currentDirection, value)
                dLat += lat
                dLong += long
            }
            'L', 'R' -> currentDirection = Direction.turn(currentDirection, dir, value)
            else -> {
                val (lat, long) = Direction.getDisplacement(dir, value)
                dLat += lat
                dLong += long
            }

        }
    }

    return abs(dLat) + abs(dLong)
}

class Direction {
    companion object {
        private const val directionsSequence = "NESW"
        private const val numberOfDirections = directionsSequence.length
        private val directionCoordinates = mapOf(
                'N' to Pair(0, 1),
                'E' to Pair(-1, 0),
                'S' to Pair(0, -1),
                'W' to Pair(1, 0)
        )

        fun getDisplacement(direction: Char, value: Int): Pair<Int, Int> {
            return Pair(
                    value * (directionCoordinates[direction]?.first ?: 0),
                    value * (directionCoordinates[direction]?.second ?: 0)
            )
        }

        fun turn(facingDirection: Char, turningDirection: Char, degrees: Int): Char {
            val dDir = (degrees % 360) / 90
            val newIdx = if (turningDirection == 'L') {
                directionsSequence.indexOf(facingDirection) - dDir + numberOfDirections
            } else {
                directionsSequence.indexOf(facingDirection) + dDir
            }
            return directionsSequence[newIdx % numberOfDirections]
        }
    }
}