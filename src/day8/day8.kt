package day8

import common.TextFileParser

fun main() {
    val instructions = TextFileParser.parseLines("src/day8/input.txt") { parseInstruction(it) }
    val program = Program(instructions)
    println("Part 1")
    val currentAccumulator = program.run().getCurrentAccumulator()
    println("Acc is $currentAccumulator")

    println("Part 2")
    if (!program.run().hasCompleted()) {
        val corruptedIndex = program.findCorruptedInstructionIndex()
        val finalAccumulator = program.getCurrentAccumulator()
        println("Acc is $finalAccumulator")
        println("Corrupted index was $corruptedIndex")
    } else {
        println("No corrupted indexes")
    }
}

fun parseInstruction(string: String): Instruction {
    val (type, value) = string.split(" ")
    return Instruction(type, value.toInt())
}

class Instruction(private val type: String, private val value: Int) {
    fun getInvertedInstruction(): Instruction? {
        if (type == "jmp") {
            return Instruction("nop", value)
        }
        if (type == "nop" && value != 0) {
            return Instruction("jmp", value)
        }
        return null
    }

    fun execute(index: Int, acc: Int): Pair<Int, Int> {
        var newIdx = index
        var newAcc = acc
        when (type) {
            "acc" -> {
                newAcc += value
                newIdx++
            }
            "jmp" -> newIdx += value
            "nop" -> newIdx++
        }
        return Pair(newIdx, newAcc)
    }
}

class Program(private val instructions: List<Instruction>) {
    private var currentIndex = 0
    private var currentAcc = 0
    private var isCompleted = false

    init {
        resetProgram()
    }

    fun getCurrentAccumulator(): Int {
        return currentAcc
    }

    fun hasCompleted(): Boolean {
        return isCompleted
    }

    private fun executeInstruction(instruction: Instruction) {
        val newValues = instruction.execute(currentIndex, currentAcc)
        updateState(newValues.first, newValues.second)
    }

    private fun updateState(newIndex: Int, newAcc: Int) {
        currentIndex = newIndex
        currentAcc = newAcc
    }

    fun run(instructions: List<Instruction> = this.instructions): Program {
        resetProgram()

        val visitedIndexes = mutableSetOf<Int>()
        while (currentIndex < instructions.size && currentIndex !in visitedIndexes && visitedIndexes.size <= instructions.size) {
            visitedIndexes.add(currentIndex)
            val instruction = instructions[currentIndex]
            val previousIndex = currentIndex
            executeInstruction(instruction)
            if (previousIndex == instructions.size - 1) {
                isCompleted = true
                return this
            }
        }
        isCompleted = visitedIndexes.size >= instructions.size
        return this
    }

    fun findCorruptedInstructionIndex(): Int? {
        for (instructionIndex in instructions.indices) {
            resetProgram()
            val newInstructions = instructions.toMutableList()
            newInstructions[instructionIndex].getInvertedInstruction()?.also { invertedInstruction ->
                newInstructions[instructionIndex] = invertedInstruction
                run(newInstructions)
                isCompleted && return instructionIndex
            }
        }
        return null
    }

    private fun resetProgram() {
        currentIndex = 0
        currentAcc = 0
        isCompleted = false
    }
}
