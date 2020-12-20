package year2020.day20

import common.TextFileParser

fun main() {
    val tiles = TextFileParser.parseGroupedData("src/year2020/day20/input.txt") { parseTile(it) }
    val puzzle = Puzzle(tiles)
    val res1 = puzzle.match().getCornerTiles()
    println(res1)
    puzzle.solvePuzzle()
    println(puzzle.calcWaterRoughness())
}

class Puzzle(private val tiles: List<Tile>) {
    lateinit var size: Pair<Int, Int>
    private val grid = mutableMapOf<Pair<Int, Int>, Tile>()

    fun match(): Puzzle {
        tiles.forEach { tile ->
            val borders = tile.getBorders().toSet()
            val otherTiles = tiles.minus(tile)
            val matches = mutableMapOf<Tile, String>()
            otherTiles.forEach { otherTile ->
                val otherTileBorders = listOf(otherTile.getBorders(), otherTile.getBorders().map { it.reversed() }).flatten()
                if (borders.intersect(otherTileBorders).isNotEmpty()) {
                    val commonBorder = borders.intersect(otherTileBorders).first()
                    matches[otherTile] = commonBorder
                }
            }
            tile.addMatches(matches)
        }
        return this
    }

    fun calcWaterRoughness(): Int {
        tiles.forEach { it.trimBorders() }
        val result = combinePieces()
        val newTile = Tile(-1, result)
        return -1
    }

    fun getCornerTiles(): Long {
        return tiles.filter { it.isCorner }.fold(1L) { acc, tile -> acc * tile.number }
    }

    private fun initializeFirstTile(): Tile {
        val firstCorner = tiles.first { it.isCorner }
        firstCorner.setAsTopLeftCorner()
        return firstCorner
    }

    private fun combinePieces(): List<String> {
        var count = 0
        val result = mutableListOf<String>()
        while (grid.filterKeys { it.first == count }.isNotEmpty()) {
            val positions = grid.filterKeys { it.first == count }.keys.sortedBy { it.second }
            val lines = grid[positions[0]]!!.trimmedValues.toMutableList()

            for (tile in 1 until positions.size) {
                val values = grid[positions[tile]]!!.trimmedValues
                values.forEachIndexed { index, value ->
                    lines[index] += value
                }
            }
            result.addAll(lines)

            count++
        }
        return result
    }

    fun solvePuzzle() {
        // hp: first tile is fixed -> change others
        val firstTile = initializeFirstTile()
        solveTile(firstTile)
    }

    private fun solveTile(currentTile: Tile, position: Pair<Int, Int> = Pair(0, 0)) {
        grid[position] = currentTile
        val adjacentTiles = currentTile.matches

        if (adjacentTiles.filter { !it.key.isSolved }.isEmpty()) {
            return
        }

        adjacentTiles.forEach { match ->
            if (!match.key.isSolved) {
                val targetTile = match.key
                var targetValue = match.value
                val currentLocation = currentTile.findBorderLocation(targetValue)
                targetValue = currentTile.getBorderAtLocation(currentLocation)
                val targetLocation = Tile.getOppositeDirection(currentLocation)
                targetTile.moveBorderToLocation(targetValue, targetLocation)
                val newPosition = Pair(position.first + currentLocation.offset.first, position.second + currentLocation.offset.second)
                solveTile(targetTile, newPosition)
            }
        }
    }

    companion object {
        private const val MONSTER_PATTERN =
                "                  # \n" +
                        "#    ##    ##    ###\n" +
                        " #  #  #  #  #  #   "
    }
}

class Tile(val number: Int, var values: List<String>) {
    var matches = mapOf<Tile, String>()
    var isCorner = false
    var isSolved = false
    val map = mutableMapOf<Pair<Int, Int>, Tile>()
    lateinit var trimmedValues: List<String>

    override fun toString(): String {
        return values.joinToString("\n")
    }

    fun trimBorders() {
        var newValues = values.toMutableList()
        newValues.removeAt(newValues.size - 1)
        newValues.removeAt(0)
        newValues = newValues.map { row -> row.subSequence(1, row.length - 1).toString() }.toMutableList()
        trimmedValues = newValues
    }

    fun setAsTopLeftCorner() {
        if (isCorner) {
            while (!isTopLeft()) {
                rotate(1)
            }
            isSolved = true
        }
    }

    private fun isTopLeft(): Boolean {
        val values = getBordersMap().filterKeys { it == Location.BOTTOM || it == Location.RIGHT }.values
        val possibleValues = matches.values.map { listOf(it, it.reversed()) }.flatten()
        return values.filter { possibleValues.contains(it) }.size == 2
    }

    fun moveBorderToLocation(target: String, targetLocation: Location) {
        if (isSolved) {
            return
        }
        val currentLocation = findBorderLocation(target)
        val currentLocationIndex = locations.indexOf(currentLocation)
        val targetLocationIndex = locations.indexOf(targetLocation)
        val rotations = (locations.size + targetLocationIndex - currentLocationIndex) % locations.size

        rotate(rotations)
        val targetBorder = getBordersMap()[targetLocation]

        if ((targetLocation == Location.LEFT || targetLocation == Location.RIGHT) && targetBorder == target.reversed()) {
            flipVertically()
        }

        if ((targetLocation == Location.TOP || targetLocation == Location.BOTTOM) && targetBorder == target.reversed()) {
            flipHorizontally()
        }
        isSolved = true
    }

    fun findBorderLocation(target: String): Location {
        val bordersLocation = getBordersMap()
        return bordersLocation.filterValues { it.contains(target) || it.reversed().contains(target) }.keys.first()
    }

    fun getBorderAtLocation(location: Location): String {
        return getBordersMap()[location]!!
    }

    fun rotate(times: Int) {
        repeat(times) {
            val rows = mutableListOf<String>()
            for (col in values[0].indices) {
                var newRow = ""
                for (row in values.indices) {
                    newRow += values[row][col]
                }
                rows.add(newRow.reversed())
            }
            values = rows
        }
    }

    fun flipHorizontally() {
        values = values.map { row -> row.reversed() }
    }

    fun flipVertically() {
        values = values.reversed()
    }

    fun addMatches(matches: Map<Tile, String>) {
        this.matches = matches
        if (matches.size == 2) {
            isCorner = true
        }
    }

    private fun getBordersMap(): Map<Location, String> {
        var leftBorder = ""
        var rightBorder = ""
        values.forEach { line ->
            leftBorder += line.first()
            rightBorder += line.last()
        }
        return mapOf(
                Location.TOP to values.first(),
                Location.BOTTOM to values.last(),
                Location.LEFT to leftBorder.reversed(),
                Location.RIGHT to rightBorder.reversed()
        )
    }

    fun getBorders(): List<String> {
        var leftBorder = ""
        var rightBorder = ""
        values.forEach { line ->
            leftBorder += line.first()
            rightBorder += line.last()
        }
        return listOf(values.first(), values.last(), leftBorder, rightBorder)
    }

    override fun hashCode(): Int {
        return number.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Tile

        if (number != other.number) return false

        return true
    }

    companion object {
        private val locations = listOf(Location.TOP, Location.RIGHT, Location.BOTTOM, Location.LEFT)

        fun getOppositeDirection(direction: Location): Location {
            val index = locations.indexOf(direction)
            return locations[(index + 2) % locations.size]
        }
    }
}

enum class Location(val offset: Pair<Int, Int>) {
    TOP(Pair(-1, 0)), RIGHT(Pair(0, 1)), BOTTOM(Pair(1, 0)), LEFT(Pair(0, -1))
}

fun parseTile(lines: List<String>): Tile {
    val number = lines[1].replace(":", "").toInt()
    val values = lines.takeLast(10)
    return Tile(number, values)
}

