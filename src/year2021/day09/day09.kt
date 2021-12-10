package year2021.day09

import common.TextFileParser
import kotlin.math.abs

fun main() {
    val dem = TextFileParser.parseLines("src/year2021/day09/input.txt") { row ->
        row.split("").filter { it.isNotEmpty() }.map { it.toInt() }.toIntArray()
    }

    println("Day 09 Part 1")
    val map = TopographicMap(dem.toTypedArray())
    val result1 = map.getTotalRiskLevel()
    println(result1)

    println("Day 09 Part 2")
    val result2 = map.getBasinSizes()
    println(result2)
}

class TopographicMap(private val map: Array<IntArray>) {
    private val rowCount = map.size
    private val colCount = map.first().size
    private val lowPoints = Array(rowCount) { Array(colCount) { -1 } }

    fun getTotalRiskLevel(): Int {
        getLowPoints()
        return lowPoints.flatten().filter { it >= 0 }.map { it + 1 }.sum()
    }

    fun getBasinSizes(): Long {
        return lowPoints.mapIndexed { x, row ->
            row.mapIndexedNotNull { y, pos ->
                if (pos >= 0) {
                    recursive(x, y)
                } else {
                    null
                }
            }
        }.flatten().sortedDescending().take(3).fold(1L) {acc, i -> acc * i }
    }

    private fun recursive(x: Int, y: Int, basinMask: Array<Array<Boolean>> = Array(rowCount) { Array(colCount) { false } }): Int? {
        if (isOutOfBasin(x, y)) {
            return null
        }
        basinMask[x][y] = true
        getAdjacentPositionsTo(x, y, basinMask).forEach { pos ->
            recursive(pos.first, pos.second, basinMask)
        }
        return basinMask.flatten().count { it }
    }

    private fun isOutOfBasin(x: Int, y: Int): Boolean {
        return getValueAtPosition(x, y) == 9
    }

    private fun getLowPoints() {
        (0 until rowCount).forEach { row ->
            (0 until colCount).forEach { col ->
                if (isLowerPoint(row, col)) lowPoints[row][col] = getValueAtPosition(row, col)
            }
        }
    }

    private fun getValueAtPosition(x: Int, y: Int) = map[x][y]

    private fun isLowerPoint(x: Int, y: Int): Boolean {
        val currentPosition = getValueAtPosition(x, y)
        return currentPosition < 9 && getAdjacentPositionsTo(x, y).map { getValueAtPosition(it.first, it.second) }
            .all { it > currentPosition }
    }

    private fun getAdjacentPositionsTo(x: Int, y: Int, basinMask: Array<Array<Boolean>>? = null): List<Pair<Int, Int>> {
        val result = mutableListOf<Pair<Int, Int>>()
        for (i in (-1..1)) {
            for (j in (-1..1)) {
                if (isAdjacentPoint(i, j) && isPointInMap(x + i, y + j) && !isInsideBasin(basinMask, x+i, y+j)) {
                    result.add(Pair(x + i, y + j))
                }
            }
        }
        return result
    }

    private fun isInsideBasin(basinMask: Array<Array<Boolean>>?, x: Int, y: Int): Boolean {
        if (basinMask == null) return false
        return basinMask[x][y]
    }

    private fun isPointInMap(x: Int, y: Int): Boolean = map.indices.contains(x) && map.first().indices.contains(y)

    private fun isAdjacentPoint(x: Int, y: Int) = abs(x) != abs(y)
}
