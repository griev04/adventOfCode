package year2021.day03

import common.TextFileParser
import kotlin.math.pow

fun main() {
    val input = TextFileParser.parseLines("src/year2021/day03/input.txt") { it }
    val report = DiagnosticReport(input.map { Reading.makeFromBinary(it) })

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

    fun getPowerConsumption(): Long {
        return gamma.getDecimalRepresentation() * epsilon.getDecimalRepresentation()
    }

    fun getLifeSupportRating(): Long = getOxygenRate() * getCO2ScrubRate()

    private fun getOxygenRate(): Long {
        return computeRate(true).readings.first().getDecimalRepresentation()
    }

    private fun getCO2ScrubRate(): Long {
        return computeRate(false).readings.first().getDecimalRepresentation()
    }

    private fun computeRate(isOxygenRate: Boolean, position: Int = 0, report: DiagnosticReport = this): DiagnosticReport {
        if (report.readingsCount == 1 || position >= report.readingSize) {
            return report
        }
        val targetValue = if (isOxygenRate) report.gamma.values[position] else report.epsilon.values[position]
        val matchedReadings = report.readings.filter { it.values[position] == targetValue }

        return computeRate(isOxygenRate, position + 1, DiagnosticReport(matchedReadings))
    }

    private fun computeGamma(): Reading {
        val result = transpose(readings).map { reading -> reading.values.sumBy { if (it) 1 else 0 }}
        return Reading.make(result.map { it >= readingsCount / 2.0 })
    }

    private fun transpose(readings: List<Reading>): List<Reading> {
        val start = readings.first().values.map { mutableListOf<Boolean>() }
        return readings.fold(start) { acc, reading ->
            reading.values.forEachIndexed { index, b ->
                acc[index].add(b)
            }
            acc
        }.map { Reading.make(it) }
    }
}

class Reading private constructor(val values: List<Boolean>) {
    fun getReadingLength(): Int = values.size

    fun getComplementaryValue(): Reading = make(values.map { !it })

    fun getDecimalRepresentation(): Long {
        return values.reversed().mapIndexed { index, b -> if (b) 2.0.pow(index.toDouble()) else 0.0 }.sum().toLong()
    }

    companion object {
        fun makeFromBinary(binaryRepresentation: String): Reading {
            return Reading(binaryRepresentation.map { it == '1' })
        }

        fun make(booleans: List<Boolean>): Reading {
            return Reading(booleans)
        }
    }
}

