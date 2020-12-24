package year2020.day17

import common.TextFileParser

fun main() {
    val referenceSystem = parseInitialConfig("src/year2020/day17/input.txt")

    println("Part 1")
    val keepActiveCondition = { adjacent: Int -> adjacent == 2 || adjacent == 3 }
    val activateCondition = { adjacent: Int -> adjacent == 3 }
    val gameOfLife = GameOfLife(referenceSystem, keepActiveCondition, activateCondition)
    gameOfLife.skipSteps(6)
    val res17 = gameOfLife.getActiveElementsCount()
    println(res17)

    println("Part 2")
    val newReferenceSystem = parseInitialConfig("src/year2020/day17/input.txt", 4)
    val gameOfLife2 = GameOfLife(newReferenceSystem, keepActiveCondition, activateCondition)
    gameOfLife2.skipSteps(6)
    val res2 = gameOfLife2.getActiveElementsCount()
    println(res2)
}

class CartesianReferenceSystem(coordinates: List<Coordinate>, private var dimensions: Int = 2) : AReferenceSystem() {
    override val coordinates: List<Coordinate> = initCoordinates(coordinates)
    override var directions: List<Coordinate> = computeSurroundingRelativePositions()

    private fun initCoordinates(coordinates: List<Coordinate>): List<Coordinate> {
        return coordinates.map {
            val new = it.values.toMutableList()
            while (new.size < dimensions) {
                new.add(0)
            }
            Coordinate(new)
        }
    }

    override fun getDimensions(): Int {
        return dimensions
    }

    private fun computeSurroundingRelativePositions(): List<Coordinate> {
        val min = Coordinate(List(dimensions) { -1 })
        val max = Coordinate(List(dimensions) { 1 })
        val items = generateNearbyPositions(min, max)
        val origin = Coordinate(List(dimensions) { 0 })
        return items.map { Coordinate(it) }.minus(origin)
    }

    private fun generateNearbyPositions(min: Coordinate, max: Coordinate): List<List<Int>> {
        if (min.values.isEmpty()) {
            return listOf(emptyList())
        }
        val allCoordinateCombinations = mutableListOf<List<Int>>()
        val missingMinimumBoundaries = min.values.toMutableList()
        val missingMaximumBoundaries = max.values.toMutableList()
        val minValue = missingMinimumBoundaries.removeAt(0)
        val maxValue = missingMaximumBoundaries.removeAt(0)

        for (i in minValue..maxValue) {
            val combinations = generateNearbyPositions(Coordinate(missingMinimumBoundaries), Coordinate(missingMaximumBoundaries))
            val updatedCombinations = combinations.map {
                val newList = it.toMutableList()
                newList.add(0, i)
                newList
            }
            allCoordinateCombinations.addAll(updatedCombinations)
        }
        return allCoordinateCombinations
    }
}

fun parseInitialConfig(fileName: String, dimensions: Int = 3): AReferenceSystem {
    val data = TextFileParser.parseLines(fileName) { it }
    val activeCubes = mutableListOf<Coordinate>()
    data.forEachIndexed { rowIndex, row ->
        row.forEachIndexed { colIndex, cube ->
            if (cube == '#') {
                activeCubes.add(Coordinate(listOf(rowIndex, colIndex)))
            }
        }
    }
    return CartesianReferenceSystem(activeCubes, dimensions)
}
