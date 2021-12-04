package year2021.day04

import java.io.File

fun main() {
    val bingo = parseBingo("src/year2021/day04/input.txt")

    println("Day 04 Part 1")
    val result1 = bingo.play().getWinningScore()
    println(result1)

    println("Day 04 Part 2")
    val newBingo = parseBingo("src/year2021/day04/input.txt")
    val result2 = newBingo.playUntilLastWinner().getWinningScore()
    println(result2)
}

class BingoSubsystem private constructor(
    private val boards: MutableList<Board>,
    private val extractionNumbers: List<Int>
) {
    private var isGameOver: Boolean = false
    private var lastExtractedNumber: Int? = null
    private var winner: Board? = null

    fun play(): BingoSubsystem {
        play(true)
        return this
    }

    fun playUntilLastWinner(): BingoSubsystem {
        play(false)
        return this
    }

    fun getWinningScore(): Int {
        return winner?.let { winner -> lastExtractedNumber?.let { number -> winner.getScore(number) } }
            ?: throw Exception("No winner yet")
    }

    private fun play(classicGameMode: Boolean = true) {
        var extractionIndex = 0
        while (!isGameOver && extractionIndex < extractionNumbers.size) {
            val currentValue = extractionNumbers[extractionIndex]
            val winningBoards = boards.filter { board -> board.markNumber(currentValue).isComplete() }

            checkForWinner(classicGameMode, winningBoards, currentValue)
            extractionIndex++
        }
    }

    private fun checkForWinner(
        classicMode: Boolean,
        winningBoards: List<Board>,
        currentValue: Int
    ) {
        if (classicMode) {
            winningBoards.firstOrNull()?.let {
                endGame(it, currentValue)
            }
        } else {
            boards.removeAll(winningBoards)
            if (boards.isEmpty()) {
                endGame(winningBoards.last(), currentValue)
            }
        }
    }

    private fun endGame(board: Board, currentValue: Int) {
        isGameOver = true
        winner = board
        lastExtractedNumber = currentValue
    }

    companion object {
        fun make(boards: List<Board>, extractedNumbers: List<Int>): BingoSubsystem {
            return BingoSubsystem(boards.toMutableList(), extractedNumbers)
        }
    }
}

class Board private constructor(private val rows: List<BoardRow>) {
    fun markNumber(number: Int): Board {
        rows.flatMap { row -> row.cells }.find { it.value == number }?.mark()
        return this
    }

    fun isComplete(): Boolean {
        return hasCompleteRow() || hasCompleteColumn()
    }

    fun getScore(extractedValue: Int): Int {
        return extractedValue * rows.sumBy { row -> row.cells.filter { v -> !v.isMarked() }.map { v -> v.value }.sum() }
    }

    private fun hasCompleteRow(): Boolean = rows.any { row -> row.isComplete() }

    private fun hasCompleteColumn(): Boolean {
        return (rows.indices).any { index ->
            BoardRow(rows.map { it.cells[index] }).isComplete()
        }
    }

    companion object {
        fun make(values: List<List<Int>>): Board {
            return Board(values.map { row -> BoardRow(row.map { BoardCell(it) }) })
        }

        private class BoardRow(val cells: List<BoardCell>) {
            fun isComplete(): Boolean = cells.all { it.isMarked() }
        }

        private class BoardCell(val value: Int) {
            private var marked: Boolean = false

            fun isMarked(): Boolean = marked

            fun mark() {
                marked = true
            }
        }
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
    return BingoSubsystem.make(boards, extractedNumbers)
}
