package year2020.day01

import common.TextFileParser

fun main() {
    val expenses = TextFileParser.parseLines("src/year2020/day01/input.txt") { line ->
        line.toInt()
    }
    val total = 2020
    println("Day 01 Part 1")
//    println(processCoupleNaive(expenses, total))
    println(processCouple(expenses, total))
    println("Day 01 Part 2")
//    println(processTripleNaive(expenses, total))
//    println(processTriple(expenses, total))
    println(processTripleRecursive(expenses, total))
}

// PART 1

fun processCoupleNaive(expenses: List<Int>, total: Int): Int {
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

fun processCouple(expenses: List<Int>, total: Int): Int? {
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

fun processTripleNaive(expenses: List<Int>, total: Int): Int {
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
fun processTriple(expenses: List<Int>, total: Int): Int? {
    expenses.forEach { expense ->
        val difference = total - expense
        val innerExpense = processCouple(expenses, difference)
        if (innerExpense != null) {
            return expense * innerExpense
        }
    }
    return null
}

fun processTripleRecursive(expenses: List<Int>, total: Int, depth: Int = 0): Int? {
    expenses.forEach { expense ->
        val difference = total - expense
        // exit condition: target reached in 2 nested levels -> 3 numbers in total
        if (difference == 0 && depth == 2) {
            return expense
        }
        if (difference > 0) {
            val innerExpense = processTripleRecursive(expenses, difference, depth + 1)
            if (innerExpense != null) {
                return expense * innerExpense
            }
        }
    }
    return null
}
