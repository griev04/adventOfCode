package day2

import common.TextFileParser

fun main() {
    val credentials = TextFileParser.parseLines("src/day2/input.txt") { line ->
        parseCredential(line) { character, firstDigit, secondDigit ->
            Policy(character, firstDigit, secondDigit)
        }
    }
    println("Part 1")
    val validCredentials = credentials.count { it.isValid() }
    println(validCredentials)
    println("Part 2")
    val credentials2 = TextFileParser.parseLines("src/day2/input.txt") { line -> parseCredential(line) { character, firstDigit, secondDigit -> PolicyPart2(character, firstDigit, secondDigit) } }
    val validCredentials2 = credentials2.count { it.isValid() }
    println(validCredentials2)
}

fun parseCredential(record: String, policyFactory: (character: Char, firstDigit: Int, secondDigit: Int) -> IPolicy): Credential {
    val recordDetails = record.split(" ")
    val (occurrencesData, characterData, password) = recordDetails
    val (firstDigit, secondDigit) = occurrencesData.split("-").map { it.toInt() }
    val character = characterData.first()
    val policy = policyFactory(character, firstDigit, secondDigit)
    return Credential(password, policy)
}

class Credential(private val password: String, private val policy: IPolicy) {
    private val isValid: Boolean = policy.isPasswordCompliant(password)

    fun isValid(): Boolean {
        return isValid
    }
}

class Policy(private val character: Char, private val minOccurrence: Int, private val maxOccurrence: Int) : IPolicy {
    override fun isPasswordCompliant(password: String): Boolean {
        val characterOccurrences = password.count { it == character }
        return characterOccurrences in minOccurrence..maxOccurrence
    }
}

class PolicyPart2(private val character: Char, private val firstPosition: Int, private val secondPosition: Int) : IPolicy {
    override fun isPasswordCompliant(password: String): Boolean {
        val firstOccurrence = password[firstPosition - 1] == character
        val secondOccurrence = password[secondPosition - 1] == character
        // xor to check if two elements are different
        return firstOccurrence xor secondOccurrence
    }
}

interface IPolicy {
    fun isPasswordCompliant(password: String): Boolean
}
