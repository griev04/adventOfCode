package year2020.day11

import common.TextFileParser

fun main() {
    var parsedGrid = TextFileParser.parseLines("src/year2020/day11/input.txt") { line -> line.map { Spot(it) } }

    println("Part 1")
    val map1 = SeatMap(parsedGrid)
    val res1 = map1.run(SeatMap.RULE_PART_1)
    println(res1)

    println("Part 2")
    // reload data from disk
    parsedGrid = TextFileParser.parseLines("src/year2020/day11/input.txt") { line -> line.map { Spot(it) } }
    val map2 = SeatMap(parsedGrid)
    val res2 = map2.run(SeatMap.RULE_PART_2)
    println(res2)
}

class Spot(symbol: Char) {
    val isSeat = symbol == 'L'
    var isOccupied = false
    var nextState = isOccupied
}

class SeatMap(rows: List<List<Spot>>) {
    private val rowCount = rows.size
    private val colCount = rows[0].size

    // use a single list instead of nested lists
    private val spotsList = rows.flatten()

    private fun toRowAndCol(index: Int): Pair<Int, Int> {
        return Pair(index / colCount, index % colCount)
    }

    private fun toIndex(row: Int, col: Int): Int {
        return row * colCount + col
    }

    private fun countOccupiedSeats(index: Int, maxDistance: Int? = null, directionsToCheck: List<Direction> = Direction.values().toList(), seen: Int = 0, currentDistance: Int = 1): Int {
        // base case: no directions to walk to or exceeded maximum distance from current position
        if (directionsToCheck.isEmpty() || (maxDistance != null && currentDistance > maxDistance)) return seen
        val (row, col) = toRowAndCol(index)

        var count = seen
        val directionsToCheckM = directionsToCheck.toMutableList()

        directionsToCheck.forEach { dir ->
            val newRow = row + currentDistance * dir.rowDelta
            val newCol = col + currentDistance * dir.colDelta
            // if target position exists
            if (newRow in 0 until rowCount && newCol in 0 until colCount) {
                val newIdx = toIndex(newRow, newCol)
                // if a set different from the current one (i.e. not in grid corner)
                if (newIdx != index && spotsList[newIdx].isSeat) {
                    if (spotsList[newIdx].isOccupied) {
                        count++
                    }
                    // first visible seat was reached
                    directionsToCheckM.remove(dir)
                }
            } else {
                // no need to continue moving in invalid positions
                directionsToCheckM.remove(dir)
            }
        }
        return countOccupiedSeats(index, maxDistance, directionsToCheckM, count, currentDistance + 1)
    }

    fun run(rule: Rule = RULE_PART_1): Int {
        var isGridChanged = true

        while (isGridChanged) {
            isGridChanged = false

            // loop over grid
            spotsList.forEachIndexed { index, spot ->
                if (spot.isSeat) {
                    val occupiedSeats = countOccupiedSeats(index, rule.maxDistance)
                    if (!spot.isOccupied && occupiedSeats == 0) {
                        spot.nextState = true
                        isGridChanged = true
                    } else if (spot.isOccupied && rule.occupancyRule(occupiedSeats)) {
                        spot.nextState = false
                        isGridChanged = true
                    } else {
                        spot.nextState = spot.isOccupied
                    }
                }
            }

            // update grid
            if (isGridChanged) {
                spotsList.forEach {
                    if (it.isSeat) {
                        it.isOccupied = it.nextState
                    }
                }
            }
        }
        return spotsList.count { it.isSeat && it.isOccupied }
    }

    private fun countAdjacentOccupations(index: Int): Int {
        val (row, col) = toRowAndCol(index)
        val startRow = maxOf(0, row - 1)
        val endRow = minOf(rowCount - 1, row + 1)
        val startCol = maxOf(0, col - 1)
        val endCol = minOf(colCount - 1, col + 1)

        val neighbors = mutableListOf<Spot>()
        for (rowIdx in startRow..endRow) {
            neighbors.addAll(spotsList.subList(toIndex(rowIdx, startCol), toIndex(rowIdx, endCol) + 1))
        }
        // remove current position
        neighbors.remove(spotsList[index])
        return neighbors.count { it.isSeat && it.isOccupied }
    }

    companion object {
        val RULE_PART_1 = Rule(1) { occupiedSeats -> occupiedSeats >= 4}
        val RULE_PART_2 = Rule(null) { occupiedSeats -> occupiedSeats >= 5 }

        class Rule (val maxDistance: Int? = null, val occupancyRule: (Int) -> Boolean)
    }
}

enum class Direction(val rowDelta: Int, val colDelta: Int) {
    NW(-1, -1),
    N(-1, 0),
    NE(-1, 1),
    E(0, 1),
    SE(1, 1),
    S(1, 0),
    SW(1, -1),
    W(0, -1)
}
