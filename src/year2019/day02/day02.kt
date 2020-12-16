package year2019.day02

import common.TextFileParser

fun main() {
    val program = TextFileParser.parseFile("src/year2019/year2020.day02/input.txt") { text ->
        Program(text.split(",").map { it.toInt() })
    }
    println("Part 1")
    val result1 = program.initializeProgram(12, 2).run().getValue(0)
    println(result1)

    println("Part 2")
    val result2 = findNounAndVerb(program, 19690720)
    println(result2)
}

fun findNounAndVerb(program: Program, target: Int): Int {
    for (noun in 0..99) {
        for (verb in 0..99) {
            program.reset().initializeProgram(noun, verb).run()
            if (program.getValue(0) == target) {
                return 100 * noun + verb
            }
        }
    }
    return -1
}

class Program(instructions: List<Int>) {
    private val sourceInstructions = instructions
    private var instructions = instructions.toMutableList()

    fun initializeProgram(noun: Int, verb: Int): Program {
        instructions[1] = noun
        instructions[2] = verb
        return this
    }

    fun reset(): Program {
        instructions = sourceInstructions.toMutableList()
        return this
    }

    fun run(): Program {
        for (pointer in instructions.indices step 4) {
            val opCode = instructions[pointer]
            if (opCode == 99) break
            executeInstruction(opCode, pointer)
        }
        return this
    }

    private fun executeInstruction(opCode: Int, pointer: Int) {
        val firstParam = instructions[pointer + 1]
        val secondParam = instructions[pointer + 2]
        val thirdParam = instructions[pointer + 3]
        when (opCode) {
            1 -> instructions[thirdParam] = instructions[firstParam] + instructions[secondParam]
            2 -> instructions[thirdParam] = instructions[firstParam] * instructions[secondParam]
        }
    }

    fun getValue(position: Int): Int {
        return instructions[position]
    }
}