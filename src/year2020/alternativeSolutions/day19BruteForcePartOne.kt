package year2020.alternativeSolutions

import common.TextFileParser

fun main() {
    val solver = parseData1("src/year2020/day19/input.txt")
    solver.solve()
    println(solver.validateMessages())
}

class SolverForAllRules(rules: List<Rule1>, private val messages: List<String>) {
    private val rules = rules.sortedBy { it.number }

    private val validRules = rules.filter { it.isSolved() }.map { it.number }.toMutableSet()

    fun solve() {
        while (validRules.size < rules.size) {
            getEligibleRules()
        }
    }

    fun validateMessages(): Int {
        val validImages = messages.filter { validateMessage(it) }
        return validImages.size
    }

    private fun validateMessage(image: String): Boolean {
        val validCombinations = rules[0].combinations.flatten()
        return validCombinations.find { it == image } != null
    }

    private fun getEligibleRules() {
        val eligibleRules = rules.filter { it.canBeCheckedFor(validRules.toList()) }
        eligibleRules.forEach { rule ->
            solveRule(rule)
            if (rule.isSolved()) {
                validRules.add(rule.number)
            }
        }
    }

    private fun solveRule(rule: Rule1): List<List<String>> {
        val allCombinationsForAllSequences = rule.sequences.map { sequence ->
            val list = sequence.map { targetRuleNumber ->
                rules[targetRuleNumber].combinations
            }
            // only one element in sequence
            if (list.size == 1) {
                list.flatten().flatten()
            } else {
                combine(list[0], list.takeLast(list.size - 1))
            }
        }
        rule.updateCombinations(allCombinationsForAllSequences)
        return allCombinationsForAllSequences
    }
}

fun combine(a: List<List<String>>, bs: List<List<List<String>>>): List<String> {
    val b = bs[0]
    val strings = mutableListOf<String>()
    for (sequenceA in a) {
        for (stringA in sequenceA) {
            for (sequenceB in b) {
                for (stringB in sequenceB) {
                    strings.add(stringA + stringB)
                }
            }
        }
    }
    if (bs.size > 1) {
        return combine(listOf(strings), bs.takeLast(bs.size - 1))
    }
    return strings
}

class Rule1(val number: Int, val sequences: List<List<Int>> = emptyList(), var combinations: List<List<String>> = emptyList()) {
    fun canBeCheckedFor(ruleNumbers: List<Int>): Boolean {
        return !isSolved() && ruleNumbers.containsAll(sequences.flatten())
    }

    fun isSolved(): Boolean {
        return combinations.isNotEmpty() && combinations[0].isNotEmpty()
    }

    fun updateCombinations(newCombinations: List<List<String>>) {
        combinations = newCombinations
    }
}

fun parseData1(fileName: String): SolverForAllRules {
    return TextFileParser.parseFile(fileName) { text ->
        val (rulesText, imagesText) = text.split("\n\n")
        val images = imagesText.split("\n")
        val rules = rulesText.split("\n").map { parseRule1(it) }
        SolverForAllRules(rules, images)
    }
}

fun parseRule1(text: String): Rule1 {
    val (ruleNumber, sequences) = text.split(": ")
    if (sequences.contains("\"")) {
        return Rule1(ruleNumber.toInt(), combinations = listOf(listOf(sequences.replace("\"", ""))))
    }
    val sequencesText = sequences.split(" | ")
    val ruleSequences = sequencesText.map { seq -> seq.split(" ").map { it.toInt() } }
    return Rule1(ruleNumber.toInt(), ruleSequences)
}
