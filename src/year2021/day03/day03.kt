package year2021.day03

import common.TextFileParser
import kotlin.math.pow

fun main() {
    val input = TextFileParser.parseLines("src/year2021/day03/input.txt") { it }
    val report = DiagnosticReport.makeFrom(input)


    println("Day 03 Part 1")
    val result1 = report.getPowerConsumption()
    println(result1)

    println("Day 03 Part 2")
    val result2 = report.getLifeSupportRating()
    println(result2)
}

class DiagnosticReport(private val readings: List<Reading>) {
    private val readingsCount = readings.size
    private val readingSize = readings.first().getReadingLength()
    private val gamma: Reading = computeGamma()
    private val epsilon: Reading = gamma.getComplementaryValue()

    private fun computeGamma(): Reading {
        val readingsCount = readingsCount
        val result = LongArray(readingSize)
        readings.forEach { r ->
            r.values.forEachIndexed { index, b -> if (b) result[index] = result[index] + 1 }
        }
        return Reading.make(result.map { it >= readingsCount / 2.0 })
    }

    fun getPowerConsumption(): Long {
        return gamma.getDecimalRepresentation() * epsilon.getDecimalRepresentation()
    }

    fun getLifeSupportRating(): Long = getOxygenRate() * getCO2ScrubRate()

    private fun getOxygenRate(): Long {
        return computeRate(false).readings.first().getDecimalRepresentation()
    }

    private fun getCO2ScrubRate(): Long {
        return computeRate(true).readings.first().getDecimalRepresentation()
    }

    private fun computeRate(isCO2: Boolean, position: Int = 0, report: DiagnosticReport = this): DiagnosticReport {
        if (report.readingsCount == 1 || position >= report.readingSize) {
            return report
        }
        val targetValue = if (isCO2) report.epsilon.values[position] else report.gamma.values[position]
        val newReadings = report.readings.filter {
            it.values[position] == targetValue
        }

        return computeRate(isCO2, position + 1, DiagnosticReport(newReadings))
    }

    companion object {
        fun makeFrom(input: List<String>): DiagnosticReport {
            return DiagnosticReport(input.map { Reading.makeFromBinary(it) })
        }
    }
}

class Reading private constructor(val values: List<Boolean>) {
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

