package year2021.day03

import common.TextFileParser
import kotlin.math.pow

fun main() {
    val input = TextFileParser.parseLines("src/year2021/day03/input.txt") { Reading.makeFromBinary(it) }

    println("Day 03 Part 1")
    val result1 = getPowerConsumption(input)
    println(result1)

    println("Day 03 Part 2")
    val result2 = ""
    println(result2)
}

fun getPowerConsumption(readings: List<Reading>): Long {
    val readingsCount = readings.size
    val result = LongArray(readings.first().getReadingLength())
    readings.forEach { r ->
        r.values.forEachIndexed { index, b -> if (b) result[index] = result[index] + 1}
    }

    val gamma = Reading.make(result.map { it > readingsCount/2 })
    val epsilon = gamma.getComplementaryValue()
    return gamma.getDecimalRepresentation() * epsilon.getDecimalRepresentation()
}


class Reading private constructor(val values: List<Boolean>){
    fun getReadingLength(): Int {
        return values.size
    }

    fun getComplementaryValue(): Reading = make(values.map { !it })

    private fun getBinaryRepresentation(): String = values.map { if (it) '1' else '0' }.toString()

    fun getDecimalRepresentation(): Long {
        return values.reversed().mapIndexed { index, b -> if (b) 2.0.pow(index.toDouble()) else 0.0 }.sum().toLong()
    }

    companion object {
        fun makeFromBinary(binaryValue: String): Reading {
            return Reading(binaryValue.map { it == '1' })
        }
        fun make(booleans: List<Boolean>): Reading {
            return Reading(booleans)
        }
    }
}
