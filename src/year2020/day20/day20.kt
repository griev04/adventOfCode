package year2020.day20

import common.TextFileParser

fun main() {
    val tiles = TextFileParser.parseGroupedData("src/year2020/day20/input.txt") { parseTile(it) }
    val puzzle = Puzzle(tiles)
    println("Part 1")
    val res1 = puzzle.getCornerTilesProduct()
    println(res1)

    println("Part 2")
    val res2 = puzzle.getWaterRoughness()
    println(res2)
}

class Puzzle(private val tiles: List<Tile>) {
    private val grid = mutableMapOf<Pair<Int, Int>, Tile>()
    private lateinit var solvedPuzzle: Tile
    private lateinit var monsterSize: Pair<Int, Int>
    private lateinit var monsterPatternPositions: List<Pair<Int, Int>>

    init {
        this.solvePuzzle()
    }

    fun getCornerTilesProduct(): Long {
        return tiles.filter { it.isCorner }.fold(1L) { acc, tile -> acc * tile.number }
    }

    fun getWaterRoughness(): Int {
        parseMonster()
        val numberOfMonsters = findMonsters(solvedPuzzle)
        return if (numberOfMonsters != 0) {
            val numberOfSymbols = solvedPuzzle.values.fold(0) { acc, row -> acc + row.count { it == '#' } }
            numberOfSymbols - numberOfMonsters * monsterPatternPositions.size
        } else {
            -1
        }
    }

    private fun solvePuzzle(): Puzzle {
        matchTiles()
        // hp: first tile is fixed and is set to be top left item of the puzzle
        val firstTile = initializeFirstTile()

        solveTile(firstTile)

        val result = combineTiles()
        solvedPuzzle = Tile(0, result)
        return this
    }

    private fun solveTile(currentTile: Tile, position: Pair<Int, Int> = 0 to 0) {
        grid[position] = currentTile
        val adjacentTiles = currentTile.matches

        if (adjacentTiles.filter { !it.key.isSolved }.isEmpty()) {
            return
        }

        adjacentTiles.forEach { match ->
            if (!match.key.isSolved) {
                var (targetTile, targetValue) = match
                val currentLocation = currentTile.findBorderLocation(targetValue)
                targetValue = currentTile.getBorderAtLocation(currentLocation)

                val targetLocation = Tile.getOppositeDirection(currentLocation)
                targetTile.arrangeToMatchingPosition(targetValue, targetLocation)

                val newPosition = position.first + currentLocation.offset.first to position.second + currentLocation.offset.second
                solveTile(targetTile, newPosition)
            }
        }
    }

    private fun initializeFirstTile(): Tile {
        val firstCorner = tiles.first { it.isCorner }
        firstCorner.setAsTopLeftCorner()
        return firstCorner
    }

    private fun combineTiles(): List<String> {
        tiles.forEach { it.trimBorders() }
        var row = 0
        val result = mutableListOf<String>()
        while (grid.filterKeys { it.first == row }.isNotEmpty()) {
            val positions = grid.filterKeys { it.first == row }.keys.sortedBy { it.second }
            val lines = grid[positions[0]]!!.trimmedValues.toMutableList()
            for (tile in 1 until positions.size) {
                val values = grid[positions[tile]]!!.trimmedValues
                values.forEachIndexed { index, value ->
                    lines[index] += value
                }
            }
            result.addAll(lines)
            row++
        }
        return result
    }

    private fun matchTiles(): Puzzle {
        tiles.forEach { tile ->
            val borders = tile.getBorders().values.toSet()
            val otherTiles = tiles.minus(tile)
            val matches = mutableMapOf<Tile, String>()
            otherTiles.forEach { otherTile ->
                val otherTileBorders = otherTile.getBorders().values.map { listOf(it, it.reversed()) }.flatten()
                if (borders.intersect(otherTileBorders).isNotEmpty()) {
                    val commonBorder = borders.intersect(otherTileBorders).first()
                    matches[otherTile] = commonBorder
                }
            }
            tile.addMatches(matches)
        }
        return this
    }

    private fun findMonsters(tile: Tile): Int {
        repeat(4) {
            tile.rotate()
            countSeaMonsters(tile.values).let { if (it > 0) return it }
        }
        tile.flipHorizontally()
        repeat(4) {
            tile.rotate()
            countSeaMonsters(tile.values).let { if (it > 0) return it }
        }
        return 0
    }

    private fun countSeaMonsters(values: List<String>): Int {
        var counter = 0
        for (row in 0 until values.size - monsterSize.first) {
            for (col in 0 until values.size - monsterSize.second) {
                if (isSeaMonster(values, row, col)) counter++
            }
        }
        return counter
    }

    private fun isSeaMonster(values: List<String>, row: Int, col: Int): Boolean {
        for (element in monsterPatternPositions) {
            if (values[row + element.first][col + element.second] != '#') return false
        }
        return true
    }

    private fun parseMonster() {
        val monsterText = MONSTER_PATTERN.split("\n")
        this.monsterSize = monsterText.size to monsterText[0].length
        monsterPatternPositions = monsterText.mapIndexed { lineIndex, line ->
            line.mapIndexed { charIndex, char ->
                if (char == '#') {
                    lineIndex to charIndex
                } else {
                    null
                }
            }.filterNotNull()
        }.flatten()
    }

    companion object {
        private const val MONSTER_PATTERN =
                "                  # \n#    ##    ##    ###\n #  #  #  #  #  #   "
    }
}

enum class Location(val offset: Pair<Int, Int>) {
    TOP(-1 to 0), RIGHT(0 to 1), BOTTOM(1 to 0), LEFT(0 to -1)
}

fun parseTile(lines: List<String>): Tile {
    val number = lines[1].replace(":", "").toInt()
    val values = lines.takeLast(10)
    return Tile(number, values)
}
