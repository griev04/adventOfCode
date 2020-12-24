package year2020.day24

import common.TextFileParser
import year2020.day17.AReferenceSystem
import year2020.day17.Coordinate
import year2020.day17.GameOfLife

fun main() {
    val sdr = parseInitialTiles("src/year2020/day24/input.txt")

    println("Part 1")
    val keepActiveCondition = { adjacent: Int -> adjacent != 0 && adjacent <= 2 }
    val activateCondition = { adjacent: Int -> adjacent == 2 }
    val gameOfLife = GameOfLife(sdr, keepActiveCondition, activateCondition)
    val res = gameOfLife.getActiveElementsCount()
    println(res)

    println("Part 2")
    gameOfLife.skipSteps(100)
    val res2 = gameOfLife.getActiveElementsCount()
    println(res2)
}

class HexagonalReferenceSystem(override val coordinates: List<Coordinate>) : AReferenceSystem() {
    override var directions: List<Coordinate> = Direction.values().map { it.coordinate }

    override fun getDimensions(): Int {
        return 2
    }

    enum class Direction(val coordinate: Coordinate) {
        W(Coordinate(listOf(0, -2))),
        NW(Coordinate(listOf(-1, -1))),
        NE(Coordinate(listOf(-1, 1))),
        E(Coordinate(listOf(0, 2))),
        SE(Coordinate(listOf(1, 1))),
        SW(Coordinate(listOf(1, -1)))
    }
}

fun parseInitialTiles(fileName: String): AReferenceSystem {
    val directions = TextFileParser.parseLines(fileName) { parseTileDirections(it) }
    val blackTiles = findBlackTiles(directions)
    return HexagonalReferenceSystem(blackTiles)
}

fun findBlackTiles(tilesDirections: List<List<HexagonalReferenceSystem.Direction>>): List<Coordinate> {
    val blackTiles = mutableSetOf<Coordinate>()
    tilesDirections.forEach { tileDirections ->
        var position = Coordinate(listOf(0, 0))
        tileDirections.forEach { direction ->
            position += direction.coordinate
        }
        if (position in blackTiles) {
            blackTiles.remove(position)
        } else {
            blackTiles.add(position)
        }
    }
    return blackTiles.toList()
}

fun parseTileDirections(directions: String): List<HexagonalReferenceSystem.Direction> {
    var index = 0
    val result = mutableListOf<HexagonalReferenceSystem.Direction>()
    while (index < directions.length) {
        val length = if (directions[index] != 'n' && directions[index] != 's') {
            1
        } else {
            2
        }
        val direction = directions.substring(index, index + length)
        index += length
        result.add(HexagonalReferenceSystem.Direction.valueOf(direction.toUpperCase()))
    }
    return result
}
