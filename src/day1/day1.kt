package day1

import common.TextFileParser

lateinit var expenses: List<Int>

fun main() {
    expenses = TextFileParser.parse("src/day1/input.txt") { line -> line.toInt()}
    val total = 2020
    println("Part 1")
    println(processCoupleNaive(total))
    println(processCouple(total))
    println("Part 2")
    println(processTripleNaive(total))
    println(processTriple(total))
    println(processTripleRecursive(total))
}

// PART 1

fun processCoupleNaive(total: Int): Int {
    var result = 0
    expenses.forEach { expense ->
        val difference = total - expense
        val isFound = expenses.find { it == difference } != null
        if (isFound) {
            result = expense * difference
        }
    }
    return result
}

fun processCouple(total: Int): Int? {
    val expenseComplements = mutableMapOf<Int, Int>()
    expenses.forEach { expense ->
        val complement = total - expense
        if (expenseComplements[complement] != null) {
            return expense * complement
        }
        expenseComplements[expense] = complement
    }
    return null
}

// PART 2

fun processTripleNaive(total: Int): Int {
    expenses.forEach { expense1 ->
        expenses.forEach { expense2 ->
            expenses.forEach { expense3 ->
                if (expense1 + expense2 + expense3 == total) {
                    return expense1 * expense2 * expense3
                }
            }
        }
    }
    return 0
}

// Make use of previous method processCouple
fun processTriple(total: Int): Int? {
    expenses.forEach { expense ->
        val difference = total - expense
        val innerExpense = processCouple(difference)
        if (innerExpense != null) {
            return expense * innerExpense
        }
    }
    return null
}

fun processTripleRecursive(total: Int, depth: Int = 0): Int? {
    expenses.forEach { expense ->
        val difference = total - expense
        // exit condition: target reached in 2 nested levels -> 3 numbers in total
        if (difference == 0 && depth == 2) {
            return expense
        }
        if (difference > 0) {
            val innerExpense = processTripleRecursive(difference, depth + 1)
            if (innerExpense != null) {
                return expense * innerExpense
            }
        }
    }
    return null
}
