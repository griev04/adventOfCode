package year2020.day04

import common.TextFileParser
import kotlin.math.roundToInt

fun main() {
    val passports = TextFileParser.parseGroupedData("src/year2020/day04/input.txt") { parsePassport(it) }

    println("Day 04 Part 1")
    val fieldRuleMapping = listOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl" ,"pid").map { it to MandatoryRule() }.toMap()
    val completePassports = countValidPassports(passports, fieldRuleMapping)
    println(completePassports)

    println("Day 04 Part 2")
    val fieldRuleMappingPart2 = mapOf(
            "byr" to MinMaxRule(1920, 2002),
            "iyr" to MinMaxRule(2010, 2020),
            "eyr" to MinMaxRule(2020, 2030),
            "hgt" to MinMaxHeightRule(150, 193),
            "hcl" to HexColorRule(),
            "ecl" to AllowedValueRule(listOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth")),
            "pid" to AllowedCharsAndLengthRule("0123456789", 9)
    )
    val validPassports = countValidPassports(passports, fieldRuleMappingPart2)
    println(validPassports)
}

private fun parsePassport(it: List<String>): Passport {
    val fields = mutableMapOf<String, String>()
    it.forEach { field ->
        val (key, value) = field.split(":")
        fields[key] = value
    }
    return Passport(fields)
}

fun countValidPassports(passports: List<Passport>, rules: Map<String, IRule>): Int {
    return passports.count { it.isValidPassport(rules) }
}

class Passport(private val fields: Map<String, String>) {
    fun isValidPassport(fieldRuleMapping: Map<String, IRule>): Boolean {
        fieldRuleMapping.forEach { (field, rule) ->
            val value = fields[field] ?: ""
            if (!rule.isValid(value)) {
                return false
            }
        }
        return true
    }
}

class MinMaxRule(private val min: Int, private val max: Int) : IRule() {
    override fun isValid(value: String): Boolean {
        if (!isFilled(value)) return false
        return value.toInt() in min..max
    }
}

class MinMaxHeightRule(private val min: Int, private val max: Int) : IRule() {
    override fun isValid(value: String): Boolean {
        if (!isFilled(value) || value.length < 3) {
            return false
        }
        val unit = value.takeLast(2)
        val height = value.take(value.length - 2)
        if (unit == "" || height == "") {
            return false
        }
        return if (unit == "in") {
            height.toInt() >= (min / 2.54).roundToInt() && height.toInt() <= (max / 2.54).roundToInt()
        } else {
            height.toInt() in min..max
        }
    }
}

class HexColorRule : IRule() {
    override fun isValid(value: String): Boolean {
        if (!isFilled(value)) return false
        return value.matches("^#(?:[0-9a-fA-F]{3}){1,2}\$".toRegex())
    }
}

class AllowedValueRule(private val allowedValues: List<String>) : IRule() {
    override fun isValid(value: String): Boolean {
        if (!isFilled(value)) return false
        return allowedValues.contains(value)
    }
}

class AllowedCharsAndLengthRule(private val allowedChars: String, private val length: Int) : IRule() {
    override fun isValid(value: String): Boolean {
        if (!isFilled(value)) return false
        return value.length == length && value.all { allowedChars.contains(it) }
    }
}

class MandatoryRule : IRule() {
    override fun isValid(value: String): Boolean {
        return isFilled(value)
    }
}

abstract class IRule {
    fun isFilled(value: String): Boolean {
        return value.isNotEmpty()
    }
    abstract fun isValid(value: String): Boolean
}
