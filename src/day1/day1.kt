package day1

import common.readTextFile

lateinit var expenses: List<Int>

fun main() {
    expenses = readTextFile("src/day1/input.txt").map { it.toInt() }
    val total = 2020
    println("Part 1")
    println(processCouple(total))
    println("Part 2")
    println(processTriple(total))
    println(processTripleRecursive(total))
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
