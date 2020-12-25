package year2020.day14

import common.TextFileParser
import kotlin.math.pow

fun main() {
    val instructions = TextFileParser.parseLines("src/year2020/day14/input.txt") { Program.parseInstruction(it) }
    println("Day 14 Part 1")
    val program = Program()
    val res = program.execute(instructions).getSumOfNumbersInMemory()
    println(res)

    println("Day 14 Part 2")
    val program2 = Program()
    val res2 = program2.execute(instructions, true).getSumOfNumbersInMemory()
    println(res2)
}

class Program {
    private var isVersionTwo = false
    private val memory: MutableMap<Long, CharArray> = mutableMapOf()
    private var currentMask: CharArray = "X".repeat(BITS).toCharArray()

    fun execute(instructions: List<Instruction>, isVersionTwo: Boolean = false): Program {
        this.isVersionTwo = isVersionTwo
        instructions.forEach { instr ->
            if (instr.type == "mask") {
                currentMask = instr.value.toCharArray()
            } else if (instr.address != null) {
                addToMemory(instr.address.toLong(), instr.value.toLong())
            }
        }
        return this
    }

    fun getSumOfNumbersInMemory(): Long {
        val binarySum = memory.values.reduce { acc, value -> binarySum(acc, value) }
        return toDecimal(binarySum)
    }

    private fun addToMemory(address: Long, value: Long) {
        if (isVersionTwo) {
            val maskedAddress = applyAddressMask(toBinary(address), currentMask)
            writeFloatingAddressValue(maskedAddress, toBinary(value))
        } else {
            val maskedValue = applyValueMask(toBinary(value), currentMask)
            writeMaskedValue(address, maskedValue)
        }
    }

    private fun writeMaskedValue(address: Long, maskedValue: CharArray) {
        memory[address] = maskedValue
    }

    private fun writeFloatingAddressValue(maskedAddress: CharArray, value: CharArray) {
        if (!maskedAddress.contains('X')) {
            memory[toDecimal(maskedAddress)] = value
            return
        }
        val volatileIndex = maskedAddress.indexOf('X')
        val poss1 = maskedAddress.copyOf()
        poss1[volatileIndex] = '0'
        val poss2 = maskedAddress.copyOf()
        poss2[volatileIndex] = '1'

        writeFloatingAddressValue(poss1, value)
        writeFloatingAddressValue(poss2, value)
        return
    }

    private fun applyValueMask(value: CharArray, mask: CharArray): CharArray {
        mask.forEachIndexed { index, char ->
            if (char != 'X') {
                value[index] = char
            }
        }
        return value
    }

    private fun applyAddressMask(address: CharArray, mask: CharArray): CharArray {
        mask.forEachIndexed { index, char ->
            if (char == '1') {
                address[index] = char
            }
            if (char == 'X') {
                address[index] = char
            }
        }
        return address
    }

    private fun binarySum(a: CharArray, b: CharArray): CharArray {
        val n1 = a.reversed()
        val n2 = b.reversed()

        var carry = 0
        var pos = 0
        var result = ""

        while (pos < n1.size || pos < n2.size || carry > 0) {
            if (pos < n1.size) {
                carry += Character.getNumericValue(n1[pos])
            }
            if (pos < n2.size) {
                carry += Character.getNumericValue(n2[pos])
            }
            result += carry % 2
            carry /= 2
            pos++
        }

        return result.reversed().toCharArray()
    }

    private fun toBinary(decimal: Long): CharArray {
        var curr = decimal
        var res = ""
        while (curr > 0) {
            res += curr % 2
            curr /= 2
        }
        return ("0".repeat(BITS - res.length) + res.reversed()).toCharArray()
    }

    private fun toDecimal(binary: CharArray): Long {
        return binary.foldIndexed(0L) { index, acc, c ->
            if (c == '0') {
                acc
            } else {
                val power = 2F.pow(binary.size - 1 - index)
                acc + power.toLong()
            }
        }
    }

    companion object {
        private const val BITS = 36

        fun parseInstruction(line: String): Instruction {
            return if (line.startsWith("mask = ")) {
                Instruction("mask", line.takeLast(BITS))
            } else {
                val (mem, value) = line.split(" = ")
                val address = mem.replace("mem[", "").replace("]", "")
                Instruction("assignment", value, address)
            }
        }
    }
}

class Instruction(val type: String, val value: String, val address: String? = null)
