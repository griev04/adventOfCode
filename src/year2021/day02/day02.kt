package year2021.day02

import common.TextFileParser

fun main() {
    val instructions = TextFileParser.parseLines("src/year2021/day02/input.txt") { Instruction.makeFrom(it) }

    println("Day 02 Part 1")
    val commandInterpretation1 = CommandInterpretation.PART_ONE
    val result1 = NavigationSystem().navigateWith(instructions, commandInterpretation1).getResult()
    println(result1)

    println("Day 02 Part 2")
    val commandInterpretation2 = CommandInterpretation.PART_TWO
    val result2 = NavigationSystem().navigateWith(instructions, commandInterpretation2).getResult()
    println(result2)
}

class NavigationSystem() {
    private val currentPosition: Position = Position(0, 0)

    fun navigateWith(courseInstructions: List<Instruction>, commandInterpretation: CommandInterpretation): NavigationSystem {
        courseInstructions.forEach { commandInterpretation(it, currentPosition)}
        return this
    }

    fun getResult(): Long = currentPosition.getPositionValue()
}

class CommandInterpretation(private val rule: (instruction: Instruction, position: Position) -> Position) {
    operator fun invoke(instruction: Instruction, position: Position): Position = rule(instruction, position)

    companion object {
        val PART_ONE = CommandInterpretation { instruction, position ->
            when (instruction.direction) {
                Instruction.Direction.FORWARD -> position.moveForward(instruction.distance)
                Instruction.Direction.UP -> position.decreaseDepth(instruction.distance)
                Instruction.Direction.DOWN -> position.increaseDepth(instruction.distance)
            }
        }

        val PART_TWO = CommandInterpretation { instruction, position ->
            when (instruction.direction) {
                Instruction.Direction.FORWARD -> {
                    position.moveForward(instruction.distance)
                    position.increaseDepthWithAim(instruction.distance)
                }
                Instruction.Direction.UP -> position.decreaseAim(instruction.distance)
                Instruction.Direction.DOWN -> position.increaseAim(instruction.distance)
            }
        }
    }
}

class Instruction private constructor(val direction: Direction, val distance: Int) {
    companion object {
        fun makeFrom(command: String): Instruction {
            val (direction, distance) = command.split(' ')
            return Instruction(Direction.valueOf(direction.toUpperCase()), distance.toInt())
        }
    }

    enum class Direction {
        FORWARD,
        UP,
        DOWN
    }
}

class Position(private var horizontalPosition: Long, private var depth: Long, private var aim: Int = 0) {
    fun moveForward(distance: Int): Position {
        horizontalPosition += distance
        return this
    }

    fun increaseDepth(distance: Int): Position {
        depth += distance
        return this
    }

    fun decreaseDepth(distance: Int): Position {
        depth -= distance
        return this
    }

    fun increaseAim(distance: Int): Position {
        aim += distance
        return this
    }

    fun decreaseAim(distance: Int): Position {
        aim -= distance
        return this
    }

    fun increaseDepthWithAim(distance: Int): Position {
        depth += distance * aim
        return this
    }

    fun getPositionValue(): Long {
        return horizontalPosition * depth
    }
}
