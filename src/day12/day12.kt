package day12

import common.TextFileParser
import kotlin.math.*

fun main() {
    val instructions = TextFileParser.parseLines("src/day12/input.txt") { Pair(it[0], it.takeLast(it.length - 1).toFloat()) }
    println("Part 1")
    val result1 = NavigationSystem(instructions)
            .navigate()
            .getManhattanDistance()
    println(result1)

    println("Part 2")
    val result2 = NavigationSystem(instructions)
            .setInitialDirectionForPart2(1, -10)
            .navigate()
            .getManhattanDistance()
    println(result2)
}

class NavigationSystem(private val instructions: List<Pair<Char, Float>>) {
    private var position = Vector(0, 0)
    private var direction = Direction.E.vector
    private var isPart2 = false

    fun setInitialDirectionForPart2(lat: Int, long: Int): NavigationSystem {
        direction = Vector(long, lat)
        isPart2 = true
        return this
    }

    fun navigate(): NavigationSystem {
        instructions.forEach { instruction ->
            val (command, value) = instruction
            when (command) {
                'F' -> position += direction * value
                'L' -> direction = direction.rotate(-value)
                'R' -> direction = direction.rotate(value)
                else -> {
                    val movingDirection = Direction.valueOf(command.toString()).vector
                    if (isPart2) {
                        direction += movingDirection * value
                    } else {
                        position += movingDirection * value
                    }
                }
            }
        }
        return this
    }

    fun getManhattanDistance(): Float {
        return position.getManhattanDistance()
    }

    private enum class Direction(val vector: Vector) {
        N(Vector(0, 1)),
        E(Vector(-1, 0)),
        S(Vector(0, -1)),
        W(Vector(1, 0))
    }
}

class Vector(private var x: Float, private var y: Float) {
    constructor(x: Int, y: Int) : this(x.toFloat(), y.toFloat())

    operator fun plus(other: Vector): Vector =
            Vector(this.x + other.x, this.y + other.y)

    operator fun times(other: Float): Vector =
            Vector(other * (this.x), other * (this.y))

    fun getManhattanDistance(): Float = abs(this.x) + abs(this.y)

    fun rotate(degrees: Float): Vector {
        val rad = degrees * PI / 180
        val newX = (this.x * cos(rad) - this.y * sin(rad)).toFloat()
        val newY = (this.x * sin(rad) + this.y * cos(rad)).toFloat()
        return Vector(newX, newY)
    }

    override fun toString(): String {
        return "($x; $y)"
    }
}
