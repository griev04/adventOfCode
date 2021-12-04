package year2021.day04

import java.io.File

fun main() {
    val bingo = parseBingo("src/year2021/day04/input.txt")

    println("Day 04 Part 1")
    val result1 = bingo.play().getWinningScore()
    println(result1)

    println("Day 04 Part 2")
    val newBingo = parseBingo("src/year2021/day04/input.txt")
    val result2 = newBingo.playUntilTheEnd().getWinningScore()
    println(result2)
}

class BingoSubsystem {
    private val boards: MutableList<Board> = mutableListOf()
    private val extractionNumbers: MutableList<Int> = mutableListOf()
    private var extraction: Int = 0
    private var hasWinner: Boolean = false
    private var winner: Board? = null

    fun withBoards(boards: List<Board>): BingoSubsystem {
        this.boards.addAll(boards)
        return this
    }

    fun withExtractedValues(values: List<Int>): BingoSubsystem {
        extractionNumbers.addAll(values)
        return this
    }

    fun play(): BingoSubsystem {
        while (!hasWinner && extraction < extractionNumbers.size) {
            val currentValue = extractionNumbers[extraction]
            boards.forEach { board ->
                board.markNumber(currentValue)
                if (board.isWinner()) {
                    hasWinner = true
                    winner = board
                    return this
                }
            }
            extraction++
        }
        return this
    }

    fun playUntilTheEnd(): BingoSubsystem {
        extraction = -1
        while(boards.isNotEmpty() && extraction < extractionNumbers.size) {
            extraction++

            val currentValue = extractionNumbers[extraction]
            val winningBoards = boards.filter { board ->
                board.markNumber(currentValue)
                board.isWinner()
            }.also { if (it.isNotEmpty()) winner = it.last() }
            boards.removeAll(winningBoards)
        }
        return this
    }

    fun getWinningScore(): Int {
        return winner?.getScore(extractionNumbers[extraction]) ?: throw Exception("No winner yet")
    }
}

class Board private constructor(private val values: List<BoardRow>){
    fun markNumber(number: Int): Board {
        values.flatMap { it.values }.find { it.value == number }?.mark()
        return this
    }

    fun isWinner(): Boolean {
        return values.any { it.isComplete() } || isAnyColumnComplete()
    }

    private fun isAnyColumnComplete(): Boolean {
        return (values.indices).any { index ->
            BoardRow(values.map { it.values[index] }).isComplete() }
    }

    fun getScore(extractedValue: Int): Int {
        return extractedValue * values.sumBy { row -> row.values.filter { v -> !v.isMarked() }.map { v -> v.value }.sum() }
    }

    companion object {
        fun make(values: List<List<Int>>): Board {
            return Board(values.map { row -> BoardRow(row.map { BoardCell(it) }) })
        }
    }
}


class BoardRow(val values: List<BoardCell>) {
    fun isComplete(): Boolean {
        return values.all { it.isMarked() }
    }
}

class BoardCell(val value: Int) {
    private var marked: Boolean = false

    fun isMarked(): Boolean = marked

    fun mark(): BoardCell {
        marked = true
        return this
    }
}

fun parseBingo(fileName: String, groupSeparator: String = "\n\n"): BingoSubsystem {
    val text = File(fileName).readText()
    val all = text.split(groupSeparator).toMutableList()
    val extractedNumbers = all.removeAt(0).split(",").map { it.toInt() }
    val boards = all.map { group ->
        val groupValues = group.split("\n").map { it.split(" ").filter { v -> v.isNotEmpty() }.map { v -> v.toInt() } }
        Board.make(groupValues)
    }
    return BingoSubsystem().withBoards(boards).withExtractedValues(extractedNumbers)
}
