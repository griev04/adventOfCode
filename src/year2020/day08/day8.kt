package year2020.day08

import common.TextFileParser

fun main() {
    val instructions = TextFileParser.parseLines("src/day8/input.txt") { parseInstruction(it) }
    val program = Program(instructions)
    println("Part 1")
    val currentAccumulator = program.run().getCurrentAccumulator()
    println("Acc is $currentAccumulator")

    println("Part 2")
    if (!program.run().hasCompleted()) {
        val finalAccumulator = program.fixMe().getCurrentAccumulator()
        println("Acc is $finalAccumulator")
    } else {
        println("No corrupted indexes")
    }
}

fun parseInstruction(string: String): Instruction {
    val (type, value) = string.split(" ")
    return Instruction(type, value.toInt())
}

class Instruction(val type: String, val value: Int) {
    var isExecuted = false
    fun getInvertedInstruction(): Instruction {
        if (type == "jmp") {
            return Instruction("nop", value)
        }
        if (type == "nop" && value != 0) {
            return Instruction("jmp", value)
        }
        return Instruction(type, value)
    }

    fun execute(program: Program) {
        when (type) {
            "acc" -> {
                program.increaseAcc(value)
                program.jump(1)
            }
            "jmp" -> program.jump(value)
            "nop" -> program.jump(1)
        }
        isExecuted = true
    }

    fun resetState(): Instruction {
        isExecuted = false
        return this
    }
}

class Program(private val instructions: List<Instruction>) {
    private var currentIndex = 0
    private var currentAcc = 0
    private var isCompleted = false

    fun getCurrentAccumulator(): Int {
        return currentAcc
    }

    fun hasCompleted(): Boolean {
        return isCompleted
    }

    fun run(): Program {
        resetProgram()

        while (!instructions[currentIndex].isExecuted) {
            val instruction = instructions[currentIndex]
            val previousIndex = currentIndex
            instruction.execute(this)
            if (previousIndex == instructions.size - 1) {
                currentIndex = previousIndex
                isCompleted = true
                return this
            }
        }
        return this
    }

    fun fixMe(): Program {
        val indicesToBeChecked = instructions.indices.filter { instructions[it].type == "nop" || instructions[it].type == "jmp" }
        for (instructionIndex in indicesToBeChecked) {
            val newInstructions = instructions.map { it.resetState() }.toMutableList()
            newInstructions[instructionIndex] = newInstructions[instructionIndex].getInvertedInstruction()
            val program = Program(newInstructions)
            program.run()
            if (program.isCompleted) {
                return program
            }
        }
        return this
    }

    private fun resetProgram() {
        currentIndex = 0
        currentAcc = 0
        isCompleted = false
    }

    fun increaseAcc(value: Int) {
        currentAcc += value
    }

    fun jump(i: Int) {
        currentIndex += i
    }
}
