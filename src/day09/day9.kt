package day09

import common.TextFileParser

fun main() {
    val input = TextFileParser.parseLines("src/day9/input.txt") { it.toLong() }
    println("Part 1")
    val result1 = findFirstNumber(input)
    println(result1)

    println("Part 2")
    if (result1 != null) {
        println(findWithUntouchedCumulativeListLinear(input, result1))
        println(findWithUntouchedCumulativeList(input, result1))
        println(findWithMobileWindow(input, result1))
        println(findWithCumulativeList(input, result1))
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

// exploit cumulative list
fun findWithUntouchedCumulativeListLinear(input: List<Long>, target: Long): Long? {
    val cumulatedList = input.cumulate()
    val cumulatedSet = cumulatedList.toSet()
    var acc = cumulatedList[0]
    var i = 1
    while (!cumulatedSet.contains(target + acc) && i < cumulatedList.size) {
        acc += input[i]
        i++
    }
    val endIndex = cumulatedList.indexOf(target + acc)
    val sequence = input.subList(i, endIndex + 1)
    return sequence.min()?.plus(sequence.max() ?: 0)
}

fun findWithUntouchedCumulativeList(input: List<Long>, target: Long): Long? {
    val cumulatedList = input.cumulate()
    var acc = cumulatedList[0]
    var i = 1
    while (cumulatedList.none { it == target + acc } && i < cumulatedList.size) {
        acc += input[i]
        i++
    }
    val endIndex = cumulatedList.indexOf(target + acc)
    val sequence = input.subList(i, endIndex + 1)
    return sequence.min()?.plus(sequence.max() ?: 0)
}


// sum with mobile window
fun findWithMobileWindow(input: List<Long>, target: Long): Long? {
    var lower = 0
    var upper = 1
    var sum = input[lower] + input[upper]

    while (sum != target) {
        if (sum < target) {
            upper++
            sum += input[upper]
        } else {
            while (sum > target) {
                sum -= input[lower]
                lower++
            }
        }
    }

    val sequence = input.subList(lower, upper + 1)
    return sequence.min()?.plus(sequence.max() ?: 0)
}

// exploit cumulative list to pop first value and subtract it from other values until unique target is found in the list
fun findWithCumulativeList(input: List<Long>, target: Long): Long? {
    var cumulatedList = input.cumulate()
    var pointer = 0

    // takes into account also cases where either zero or more than one solutions are possible
    while (cumulatedList.none { it == target } && cumulatedList.isNotEmpty()) {
        cumulatedList = cumulatedList.subtractToAllElements(cumulatedList[0]).toMutableList()
        cumulatedList.removeAt(0)
        pointer++
    }

    if (cumulatedList.isEmpty()) {
        return null
    }

    val sequenceSize = cumulatedList.indexOf(target)
    val sequence = cumulatedList.take(sequenceSize).revertCumulate()
    return sequence.min()?.plus(sequence.max() ?: 0)
}

fun List<Long>.cumulate(): List<Long> {
    var previousVal = 0L
    return this.map {
        val newValue = it + previousVal
        previousVal += it
        newValue
    }
}

fun List<Long>.revertCumulate(): List<Long> {
    val reversedList = this.reversed()
    return reversedList.mapIndexed { index, l ->
        if (index < reversedList.size - 1) {
            l - reversedList[index + 1]
        } else {
            l
        }
    }
}

fun List<Long>.subtractToAllElements(value: Long): List<Long> {
    return this.map { it - value }
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
