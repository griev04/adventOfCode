package year2021.day08

import common.TextFileParser

fun main() {
    val entries = TextFileParser.parseLines("src/year2021/day08/input.txt") { Entry.makeFrom(it) }

    println("Day 08 Part 1")
    val result1 = entries.sumBy { it.countUniqueDigits() }
    println(result1)

    println("Day 08 Part 2")
    val result2 = entries.sumBy { it.getDecodedNumber() }
    println(result2)
}

class Entry(private val patterns: Set<String>, private val encodedDigits: List<String>) {

    fun countUniqueDigits(): Int {
        val okLengths = lengthToDigitMap.map { it.first }
        return encodedDigits.map {
            if (okLengths.contains(it.length)) 1 else 0
        }.sum()
    }

    fun getDecodedNumber() = decodePattern()

    private fun decodePattern(): Int {
        val segments = defineSegments()

        val digitBySegments = mapSegmentsToDigits(segments)

        return encodedDigits.map { string -> digitBySegments[string.toSet()] }
            .joinToString("").toInt()
    }

    private fun mapSegmentsToDigits(segments: CharArray) =
        segmentsByDigit.map { s -> s.map { c -> segments[c.toString().toInt()] }.toSet() }
            .mapIndexed { index, chars -> chars to index }.toMap()

    private fun defineSegments(): CharArray {
        // define unique numbers
        val numbers = lengthToDigitMap.associate { entry ->
            entry.second to patterns.first { it.length == entry.first }.toSet()
        }

        val segments = CharArray(7) { ' ' }
        // find segment 0
        segments[0] = (numbers[7]!! - numbers[1]!!).first()

        // remove segment 0 and map others
        val patternsByLength = patterns.joinToString("").filter { it != segments[0] }.groupBy { it }
            .map { entry -> entry.value.size to entry.key }.toMap()

        // find missing segments
        segments[1] = patternsByLength[6]!!
        segments[2] = patternsByLength[8]!!
        segments[4] = patternsByLength[4]!!
        segments[5] = patternsByLength[9]!!
        segments[3] = (numbers[4]!! - segments[1] - segments[2] - segments[5]).first()
        segments[6] = numbers[8]!!.minus(segments.toSet()).first()

        return segments
    }

    companion object {
        fun makeFrom(rawEntry: String): Entry {
            val (patterns, digits) = rawEntry.split(" | ").map { it.split(" ") }
            return Entry(patterns.toSet(), digits)
        }

        val lengthToDigitMap = listOf((2 to 1), (4 to 4), (3 to 7), (7 to 8))

        val segmentsByDigit =
            listOf("012456", "25", "02346", "02356", "1235", "01356", "013456", "025", "0123456", "012356")
    }
}
