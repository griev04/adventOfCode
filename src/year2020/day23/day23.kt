package year2020.day23

import common.TextFileParser
import java.math.BigInteger

fun main() {
    val cups = TextFileParser.parseFile("src/year2020/day23/input.txt") { parseCups(it) }

    println("Day23 Part 1")
    val game = CupsGame(cups, 100)
    val res1 = game.playGame().getResultingList()
    println(res1)

    println("Day23 Part 2")
    val newCups = updateCupsUpTo(cups, 1000000)
    val game2 = CupsGame(newCups, 10000000)
    val res2 = game2.playGame().getResultPart2()
    println(res2)
}

class CupsGame(private val cups: List<Int>, private val moves: Int) {
    private lateinit var nextLabels: IntArray
    private var current: Int = 0
    private var minimumLabel = cups.min() ?: 0
    private var maximumLabel = cups.max() ?: 0

    fun playGame(): CupsGame {
        // map array to values: each index is a label and its value is the next label
        nextLabels = IntArray(cups.size + 1) { it }
        cups.withIndex().forEach { cup ->
            nextLabels[cup.value] = cups[(cup.index + 1) % cups.size]
        }
        // set current index as element previous to the first label
        current = nextLabels.indexOf(cups[0])

        repeat(moves) {
            playMove()
        }
        return this
    }

    fun getResultingList(): String {
        var res = ""
        var index = nextLabels.indexOf(1)
        repeat(nextLabels.size - 1) {
            res += nextLabels[index]
            index = nextLabels[index]
        }
        return res
    }

    fun getResultPart2(): BigInteger {
        val first = nextLabels[1]
        val second = nextLabels[first]
        return first.toBigInteger() * second.toBigInteger()
    }

    private fun playMove() {
        val currentLabel = nextLabels[current]
        val first = nextLabels[currentLabel]
        val second = nextLabels[first]
        val third = nextLabels[second]
        // update next item
        val nextLabel = nextLabels[third]
        nextLabels[currentLabel] = nextLabel
        // add back removed items
        val newDestination = findDestinationLabel(listOf(first, second, third))
        nextLabels[third] = nextLabels[newDestination]
        nextLabels[newDestination] = first
        // move to next item
        current = currentLabel
    }

    private fun findDestinationLabel(removedValues: List<Int>): Int {
        var targetValue = nextLabels[current] - 1
        while (true) {
            when {
                (targetValue in removedValues) -> targetValue--
                (targetValue < minimumLabel) -> targetValue = maximumLabel
                else -> return targetValue
            }
        }
    }
}

fun updateCupsUpTo(cups: List<Int>, max: Int): List<Int> {
    val newCups = cups.toMutableList()
    newCups.addAll((newCups.max()!! + 1)..max)
    return newCups
}

fun parseCups(text: String): List<Int> {
    return text.split("").filter { it.isNotEmpty() && it != "\n" }.map { it.toInt() }
}
