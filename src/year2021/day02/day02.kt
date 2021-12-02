package year2021.day02

import common.TextFileParser

fun main() {
    val input = TextFileParser.parseLines("src/year2021/day02/input.txt") { Instruction.makeFrom(it) }

    println("Day 02 Part 1")
    val result1 = NavigationSystem().navigateWith(input).getResult()
    println(result1)
}

class NavigationSystem() {
    private val course: MutableList<Instruction> = mutableListOf()
    private val currentPosition: Position = Position(0, 0)

    fun navigateWith(courseInstructions: List<Instruction>): NavigationSystem {
        course.clear()
        course.addAll(courseInstructions)
        navigate()
        return this
    }

    private fun navigate() {
        course.forEach { instruction -> instruction.executeAt(currentPosition) }
    }

    fun getResult(): Long = currentPosition.getPositionValue()
}

class Position(private var horizontalPosition: Long, private var depth: Long) {
    fun moveForward(distance: Int) {
        horizontalPosition += distance
    }

    fun increaseDepth(distance: Int) {
        depth += distance
    }

    fun decreaseDepth(distance: Int) {
        depth -= distance
    }

    fun getPositionValue(): Long {
        return horizontalPosition * depth
    }
}

class Instruction private constructor(private val direction: Direction, private val distance: Int) {
    fun executeAt(position: Position) {
        return when (direction) {
            Direction.FORWARD -> position.moveForward(distance)
            Direction.UP -> position.decreaseDepth(distance)
            Direction.DOWN -> position.increaseDepth(distance)
        }
    }

    companion object {
        fun makeFrom(command: String): Instruction {
            val (direction, distance) = command.split(' ')
            return Instruction(Direction.valueOf(direction.toUpperCase()), distance.toInt())
        }
    }
}

enum class Direction {
    FORWARD,
    UP,
    DOWN
}