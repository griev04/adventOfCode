package day12

import common.TextFileParser
import kotlin.math.*

fun main() {
    val instructions = TextFileParser.parseLines("src/day12/input.txt") { Pair(it[0], it.takeLast(it.length - 1).toInt()) }
    println("Part 1")
    println(findDistance(instructions))

    println("Part 2")
    println(findDistance(instructions, true))
}

fun findDistance(instructions: List<Pair<Char, Int>>, isPart2: Boolean = false): Int {
    var direction = if (isPart2) {
        Vector(1, -10)
    } else {
        Direction.Companion.DirectionVector.E.vector
    }

    var position = Vector(0, 0)

    instructions.forEach { instruction ->
        val (command, value) = instruction
        when (command) {
            'F' -> {
                val new = Direction.getDisplacement(direction, value)
                position += new
            }
            'L', 'R' -> direction = direction.rotate(value, command)
            else -> {
                val movingDirection = Direction.Companion.DirectionVector.valueOf(command.toString()).vector
                val new = Direction.getDisplacement(movingDirection, value)
                if (isPart2) {
                    direction += new
                } else {
                    position += new
                }
            }
        }
    }

    return position.getManhattanDistance()
}

class Vector(private val lat: Int, private val long: Int) {
    operator fun plus(other: Vector): Vector =
            Vector(this.lat + other.lat, this.long + other.long)

    operator fun times(other: Int): Vector =
            Vector(other * (this.lat), other * (this.long))

    fun getManhattanDistance(): Int = abs(this.lat) + abs(this.long)

    fun rotate(degrees: Int, rotationDirection: Char): Vector {
        val signedDegrees = if (rotationDirection == 'L') {
            -degrees
        } else {
            degrees
        } * PI / 180
        val newLong = round(this.long * cos(signedDegrees) - this.lat * sin(signedDegrees)).toInt()
        val newLat = round(this.long * sin(signedDegrees) + this.lat * cos(signedDegrees)).toInt()
        return Vector(newLat, newLong)
    }

    override fun toString(): String {
        return "lat: $lat, long: $long"
    }
}

class Direction {
    companion object {
        enum class DirectionVector(val vector: Vector) {
            N(Vector(1, 0)),
            E(Vector(0, -1)),
            S(Vector(-1, 0)),
            W(Vector(0, 1))
        }

        fun getDisplacement(vector: Vector, value: Int): Vector {
            return vector * value
        }
    }
}