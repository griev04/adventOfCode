package day12

import common.TextFileParser
import kotlin.math.abs

fun main() {
    val instructions = TextFileParser.parseLines("src/day12/input.txt") { Pair(it[0], it.takeLast(it.length - 1).toInt()) }
    println("Part 1")
    println(findDistance(instructions))

    println("Part 2")
    println(findDistance(instructions, true))
}

fun findDistance(instructions: List<Pair<Char, Int>>, isPart2: Boolean = false): Int {
    var currentVector = if (isPart2) {
        Pair(1, -10)
    } else {
        Direction.Companion.DirectionVector.E.vector
    }
    var dLat = 0; var dLong = 0

    instructions.forEach { instruction ->
        val (command, value) = instruction
        when (command) {
            'F' -> {
                val (lat, long) = Direction.getDisplacement(currentVector, value)
                dLat += lat; dLong += long
            }
            'L', 'R' -> currentVector = Direction.turn(currentVector, command, value)
            else -> {
                val direction = Direction.Companion.DirectionVector.valueOf(command.toString()).vector
                val (lat, long) = Direction.getDisplacement(direction, value)
                if (isPart2) {
                    currentVector = Pair(currentVector.first + lat, currentVector.second + long)
                } else {
                    dLat += lat; dLong += long
                }
            }
        }
    }

    return abs(dLat) + abs(dLong)
}

class Direction {
    companion object {
        enum class DirectionVector(val vector: Pair<Int, Int>) {
            N(Pair(1, 0)),
            E(Pair(0, -1)),
            S(Pair(-1, 0)),
            W(Pair(0, 1))
        }

        fun getDisplacement(vector: Pair<Int, Int>, value: Int): Pair<Int, Int> {
            return Pair(value * (vector.first), value * (vector.second))
        }

        fun turn(facingDirection: Pair<Int, Int>, turningDirection: Char, degrees: Int): Pair<Int, Int> {
            val turns = (degrees % 360) / 90
            if (turns == 0) return facingDirection

            var newDirection = facingDirection

            for (t in 1..turns) {
                newDirection = if (turningDirection == 'R') {
                    Pair(newDirection.second, -newDirection.first)
                } else {
                    Pair(-newDirection.second, newDirection.first)
                }
            }
            return newDirection
        }
    }
}