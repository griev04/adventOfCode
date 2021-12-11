package year2021.day10

import common.TextFileParser
import java.util.*

fun main() {
    val input = TextFileParser.parseLines("src/year2021/day10/input.txt") { l -> l.map { Token.make(it) } }

    println("Day 10 Part 1")
    val parser = Parser(input)
    val result1 = parser.getCorruptedLinesScore()
    println(result1)

    println("Day 10 Part 2")
    val result2 = parser.getIncompleteLinesScore()
    println(result2)
}

class Token(value: Char) {
    val value: Char
    val isOpeningToken: Boolean
    var corruptScore = 0
    var incompleteScore = 0
    private var closingToken: Token? = null

    init {
        require(value in OPENERS || value in CLOSERS) {
            throw Exception("Invalid token")
        }
        this.value = value
        this.isOpeningToken = value in OPENERS

        if (!isOpeningToken) {
            corruptScore = scores.getOrDefault(value, 0)
            incompleteScore = closeScores.getOrDefault(value, 0)
        } else {
            closingToken = make(CLOSERS[OPENERS.indexOf(value)])
        }
    }

    fun getClosingToken(): Token {
        if (!isOpeningToken || closingToken == null) {
            throw Exception("Already closing token")
        }
        return closingToken as Token
    }

    override fun equals(other: Any?): Boolean {
        other as Token
        return other.value == value
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }

    companion object {
        private val allTokens = mutableSetOf<Token>()
        fun make(value: Char): Token {
            return allTokens.find { it.value == value } ?: Token(value).also { allTokens.add(Token(value)) }
        }

        private const val OPENERS = "([{<"
        private const val CLOSERS = ")]}>"
        private val scores = mapOf(')' to 3, ']' to 57, '}' to 1197, '>' to 25137)
        private val closeScores = mapOf(')' to 1, ']' to 2, '}' to 3, '>' to 4)
    }
}

class Parser(input: List<List<Token>>) {
    private var validLines: List<List<Token>>
    private val corruptedTokens = mutableMapOf<Token, Int>()
    private var missingCharsScores = mutableListOf<Long>()

    init {
        validLines = input.filter { checkLine(it) }
    }

    fun getCorruptedLinesScore(): Int {
        return corruptedTokens.entries.fold(0) { acc, entry ->
            val (char, count) = entry
            acc + char.corruptScore * count
        }
    }

    fun getIncompleteLinesScore() = missingCharsScores.sorted()[missingCharsScores.size / 2]

    private fun checkLine(line: List<Token>): Boolean {
        var i = 0
        val stack = TokenStack()
        while (i in line.indices) {
            val current = line[i]
            kotlin.runCatching {
                stack.handle(current)
            }.getOrElse {
                corruptedTokens[current] = corruptedTokens.getOrDefault(current, 0) + 1
                return false
            }
            i++
        }
        if (!stack.isEmpty()) {
            updateMissingScore(stack)
        }
        return true
    }

    private fun updateMissingScore(stack: TokenStack) {
        missingCharsScores.add(stack.getMissingTokens().fold(0L) { acc, it ->
            acc * 5 + it.incompleteScore
        })
    }
}

class TokenStack : Stack<Token>() {
    private val values = mutableListOf<Token>()

    fun handle(element: Token) {
        if (element.isOpeningToken) {
            add(element)
        } else {
            close(element)
        }
    }

    fun getMissingTokens(): List<Token> {
        return values.map { it.getClosingToken() }.reversed()
    }

    override fun add(element: Token): Boolean {
        return values.add(element)
    }

    private fun close(element: Token): Token {
        if (values.last().getClosingToken() != element) {
            throw Exception("Corrupted")
        }
        return pop()
    }

    override fun pop(): Token {
        return values.removeAt(values.lastIndex)
    }

    override fun isEmpty(): Boolean {
        return values.isEmpty()
    }
}