package year2021.day08

import common.TextFileParser

fun main() {
    val entries = TextFileParser.parseLines("src/year2021/day08/input.txt") { Entry.makeFrom(it) }

    println("Day 08 Part 1")
    val result1 = entries.map { it.countUniqueDigits() }.sum()
    println(result1)

    println("Day 08 Part 2")
    val result2 = ""
    println(result2)
}

class Entry(private val patterns: Set<String>, private val encodedDigits: List<String>) {
    private lateinit var digits: List<Int>

    init {
        decode()
        countUniqueDigits()
    }

    fun countUniqueDigits(): Int {
        val okLengths = lengthToDigitMap.keys
        return encodedDigits.map {
            if (okLengths.contains(it.length)) 1 else 0
        }.sum()
    }

    private fun decode() {
        val numbers = mutableMapOf<Int, Set<Char>>()
        val segments = Array(7) { ' ' }
        lengthToDigitMap.entries.forEach { entry ->
            numbers[entry.value] = patterns.first { it.length == entry.key }.toSet()
        }
        segments[0] = (numbers[7]!! - numbers[1]!!).first()
        val n690 = patterns.filter { it.length == 6 }
        segments[5] = n690.fold(n690.first().toSet()) { acc, s ->
            acc.intersect(s.toSet())
        }.intersect(numbers[1]!!).first()
        segments[2] = numbers[1]!!.minus(segments[5]).first()
    }

    companion object {
        fun makeFrom(rawEntry: String): Entry {
            val (patterns, digits) = rawEntry.split(" | ").map { it.split(" ") }
            return Entry(patterns.toSet(), digits)
        }

        val lengthToDigitMap = listOf((2 to 1), (4 to 4), (3 to 7), (7 to 8)).toMap()
    }
}