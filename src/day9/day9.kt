package day9

import common.TextFileParser

fun main() {
    val input = TextFileParser.parseLines("src/day9/input.txt") { it.toLong() }
    println("Part 1")
    val result1 = findFirstNumber(input)
    println(result1)

    println("Part 2")
    if (result1 != null) {
        println(findWithCumulation(input, result1))
        println(findBruteForce(input, result1))
    }
}

// Part 1
fun findFirstNumber(input: List<Long>, preamble: Int = 25): Long? {
    for (i in preamble until input.size) {
        val subList = input.subList(i - preamble, i)
        if (!naiveParsing(subList, input[i])) {
            return input[i]
        }
    }
    return null
}

fun naiveParsing(input: List<Long>, target: Long): Boolean {
    input.forEach { a ->
        val b = target - a
        val found = input.find { it == b } != null && a != b
        if (found) {
            return true
        }
    }
    return false
}

// Part 2

// exploit cumulative list to pop first value and subtract it from other values until target is found in the list
fun findWithCumulation(input: List<Long>, target: Long): Long? {
    var cumulatedList = cumulate(input).toMutableList()
    var pointer = 0

    while (cumulatedList.size > 0 && !(cumulatedList.contains(target) && !isValidResult(cumulatedList, target))) {
        cumulatedList = cumulatedList.map { it - cumulatedList[0] }.toMutableList()
        cumulatedList.removeAt(0)
        pointer++
    }

    if (cumulatedList.size == 0) {
        return null
    }

    val subListEnd = cumulatedList.indexOf(target)
    val sequence = decumulate(cumulatedList.take(subListEnd))
    return sequence.min()?.plus(sequence.max() ?: 0)
}

private fun isValidResult(cumulatedList: MutableList<Long>, target: Long) =
        decumulate(cumulatedList.take(cumulatedList.indexOf(target))).sum() == target

private fun cumulate(input: List<Long>): List<Long> {
    var previousVal = 0L
    return input.map {
        val newValue = it + previousVal
        previousVal += it
        newValue
    }
}

private fun decumulate(input: List<Long>): List<Long> {
    val reversedList = input.reversed()
    return reversedList.mapIndexed { index, l ->
        if (index < reversedList.size - 1) {
            l - reversedList[index + 1]
        } else {
            l
        }
    }
}

fun findBruteForce(input: List<Long>, target: Long): Long? {
    for (index in input.indices) {
        var acc = 0L
        var accIndex = index
        while (acc < target) {
            acc += input[accIndex]
            accIndex++
        }
        if (acc == target) {
            val sequence = input.subList(index, accIndex)
            return sequence.min()?.plus(sequence.max() ?: 0)
        }
    }
    return null
}
