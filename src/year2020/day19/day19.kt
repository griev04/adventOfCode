package year2020.day19

import common.TextFileParser

fun main() {
    val messageValidator = parseData("src/year2020/day19/input.txt")
    println("Part 1")
    val validMessages = messageValidator.countValidMessages()
    println(validMessages)

    println("Part 2")
    messageValidator.updateRulesForPart2(listOf("8: 42 | 42 8", "11: 42 31 | 42 11 31"))
    val validMessages2 = messageValidator.countValidMessages()
    println(validMessages2)
}

class MessageValidator(rules: List<Rule>, private val messages: List<String>) {
    private val rules = rules.map { it.number to it }.toMap().toMutableMap()

    fun countValidMessages(): Int {
        return messages.filter { computePossibleMessages(it).contains(it) }.size
    }

    private fun computePossibleMessages(message: String, combinations: List<String> = emptyList(), rule: Rule = rules[0]!!, currentLength: Int = 1): List<String> {
        // input may have loops, so a limit has to be set
        if (currentLength + 4 > message.length) return emptyList()

        if (rule.value != null) {
            return if (combinations.isEmpty()) {
                listOf(rule.value)
            } else {
                combinations.map { it + rule.value }.filter { isValidSubset(message, it) }
            }
        }
        val ruleCombinations = mutableListOf<String>()
        for (sequence in rule.sequences) {
            var sequenceCombinations = combinations.toList()
            for (element in sequence) {
                val ruleToApply = rules[element]!!
                sequenceCombinations = computePossibleMessages(message, sequenceCombinations, ruleToApply, currentLength + sequence.size)
            }
            ruleCombinations.addAll(sequenceCombinations)
        }
        return ruleCombinations
    }

    private fun isValidSubset(message: String, result: String): Boolean {
        return message.startsWith(result) && result.length <= message.length
    }

    fun updateRulesForPart2(lines: List<String>): MessageValidator {
        lines.forEach { line ->
            val newRule = parseRule(line)
            rules[newRule.number] = newRule
        }
        return this
    }
}

fun parseData(fileName: String): MessageValidator {
    return TextFileParser.parseFile(fileName) { text ->
        val (rulesText, imagesText) = text.split("\n\n")
        val images = imagesText.split("\n")
        val rules = rulesText.split("\n").map { parseRule(it) }
        MessageValidator(rules, images)
    }
}

fun parseRule(text: String): Rule {
    val (ruleNumber, sequences) = text.split(": ")
    if (sequences.contains("\"")) {
        return Rule(ruleNumber.toInt(), value = sequences.replace("\"", ""))
    }
    val sequencesText = sequences.split(" | ")
    val ruleSequences = sequencesText.map { seq -> seq.split(" ").map { it.toInt() } }
    return Rule(ruleNumber.toInt(), ruleSequences)
}

class Rule(val number: Int, val sequences: List<List<Int>> = emptyList(), val value: String? = null)
