package year2020.alternativeSolutions

import common.TextFileParser
import year2020.day23.parseCups

fun main() {
    val cups = TextFileParser.parseFile("src/year2020/day23/input.txt") { parseCups(it) }

    println("Part 1")
    val res = play(cups)
    println(res)
}

fun play(cupsInput: List<Int>): String {
    val cups = cupsInput.toMutableList()
    var currentCup = cups[0]
    for (move in 1..100) {
        playMove(cups, currentCup)
        val newCupIndex = getNextIndexOf(cups, currentCup)
        currentCup = cups[newCupIndex]
    }
    return getResult(cups)
}

fun getResult(cupsSequence: List<Int>): String {
    val cups = cupsSequence.toMutableList()
    val target = cups.indexOf(1)
    for (i in 0..target) {
        val removed = cups.removeAt(0)
        if (i != target) {
            cups.add(removed)
        }
    }
    return cups.joinToString("")
}

fun getNextIndexOf(cups: List<Int>, target: Int): Int {
    return (cups.indexOf(target) + 1) % cups.size
}

fun playMove(cups: MutableList<Int>, currentCup: Int) {
    val removedCups = mutableListOf<Int>()
    repeat(3) {
        removedCups.add(cups.removeAt(getNextIndexOf(cups, currentCup)))
    }
    val destination = findDestination(cups, currentCup)
    cups.addAll(cups.indexOf(destination) + 1, removedCups)
}

fun findDestination(cups: List<Int>, current: Int): Int? {
    var destination: Int? = null
    var currentTarget = current - 1
    val minimum = cups.min()
    while (destination == null) {
        minimum?.let { if (currentTarget < it) return cups.max() }
        destination = cups.find { it == currentTarget }
        currentTarget--
    }
    return destination
}
