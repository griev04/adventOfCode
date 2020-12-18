package year2020.day02

import common.TextFileParser

fun main() {
    println("Part 1")
    val result = countValidCredentials("src/year2020/day02/input.txt")
    println(result)

    println("Part 2")
    val result2 = countValidCredentials("src/year2020/day02/input.txt", true)
    println(result2)
}

private fun countValidCredentials(fileName: String, isSecondPart: Boolean = false): Int {
    val credentials = parseCredentialsFromFile(fileName, isSecondPart)
    return credentials.count { it.isValid() }
}

private fun parseCredentialsFromFile(fileName: String, isSecondPart: Boolean) =
        TextFileParser.parseLines(fileName) { line ->
            parseCredential(line) { character, firstDigit, secondDigit ->
                if (isSecondPart) {
                    PolicyPart2(character, firstDigit, secondDigit)
                } else {
                    Policy(character, firstDigit, secondDigit)
                }
            }
        }

private fun parseCredential(record: String, policyFactory: (character: Char, firstDigit: Int, secondDigit: Int) -> IPolicy): Credential {
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
