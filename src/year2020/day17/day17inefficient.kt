package year2020.day17

import common.TextFileParser

fun main() {
    val referenceSystem = parseInitialConfigData("src/year2020/day17/input.txt")
    println("Part 1")
    val energySource = EnergySource(referenceSystem)
    val result = energySource.runCycles(6).getActiveCubesCount()
    println(result)
    println("Part 2")
    val newReferenceSystem = parseInitialConfigData("src/year2020/day17/input.txt", 4)
    val energySource2 = EnergySource(newReferenceSystem)
    val result2 = energySource2.runCycles(6).getActiveCubesCount()
    println(result2)
}

class EnergySource(referenceSystem: ReferenceSystem) {
    private val activeCubes: MutableSet<Coordinate> = referenceSystem.coordinates.toMutableSet()
    private val previousActiveCubes = activeCubes.toMutableSet()

    private val dimensions = referenceSystem.getDimensions()
    private lateinit var spaceBoundaries: Pair<Coordinate, Coordinate>

    init {
        updateSpaceBoundaries()
    }

    fun getActiveCubesCount(): Int {
        return activeCubes.size
    }

    fun runCycles(cycles: Int): EnergySource {
        for (cycle in 1..cycles) {
            runCycle()
            previousActiveCubes.clear()
            previousActiveCubes.addAll(activeCubes)
            updateSpaceBoundaries()
        }
        return this
    }

    private fun runCycle() {
        val (minimumBoundary, maximumBoundary) = spaceBoundaries
        val positionsToBeChecked = generateNearbyPositions(minimumBoundary, maximumBoundary, 1).map { Coordinate(it) }

        val previouslyActivePositions = positionsToBeChecked.intersect(previousActiveCubes)
        val previouslyInactivePositions = positionsToBeChecked.minus(previouslyActivePositions)

        previouslyActivePositions.forEach { position ->
            val nearbyActiveCubes = findNearbyActiveCubes(position)
            if (nearbyActiveCubes != 2 && nearbyActiveCubes != 3) {
                activeCubes.remove(position)
            }
        }

        previouslyInactivePositions.forEach { position ->
            val nearbyActiveCubes = findNearbyActiveCubes(position)
            if (nearbyActiveCubes == 3) {
                activeCubes.add(position)
            }
        }
    }

    private fun findNearbyActiveCubes(coordinate: Coordinate): Int {
        val surroundingPositions = computeSurroundingPositions(coordinate)
        val intersection = previousActiveCubes.intersect(surroundingPositions).minus(coordinate)
        return intersection.size
    }

    private fun computeSurroundingPositions(currentPosition: Coordinate): Set<Coordinate> {
        val relativePositions = computeSurroundingRelativePositions()
        val absolutePositions = relativePositions.map { Coordinate(it) + currentPosition }
        return absolutePositions.toSet().minus(currentPosition)
    }

    private fun computeSurroundingRelativePositions(): List<List<Int>> {
        val min = Coordinate(List(dimensions) { -1 })
        val max = Coordinate(List(dimensions) { 1 })
        return generateNearbyPositions(min, max)
    }

    private fun generateNearbyPositions(min: Coordinate, max: Coordinate, expansion: Int = 0): List<List<Int>> {
        if (min.values.isEmpty()) {
            return listOf(emptyList())
        }
        val allCoordinateCombinations = mutableListOf<List<Int>>()
        val missingMinimumBoundaries = min.values.toMutableList()
        val missingMaximumBoundaries = max.values.toMutableList()
        val minValue = missingMinimumBoundaries.removeAt(0) - expansion
        val maxValue = missingMaximumBoundaries.removeAt(0) + expansion

        for (i in minValue..maxValue) {
            val combinations = generateNearbyPositions(Coordinate(missingMinimumBoundaries), Coordinate(missingMaximumBoundaries), expansion)
            val updatedCombinations = combinations.map {
                val newList = it.toMutableList()
                newList.add(0, i)
                newList
            }
            allCoordinateCombinations.addAll(updatedCombinations)
        }
        return allCoordinateCombinations
    }

    private fun updateSpaceBoundaries() {
        val min = mutableListOf<Int>()
        val max = mutableListOf<Int>()
        for (dim in 0 until dimensions) {
            min.add(activeCubes.minBy { it.values[dim] }?.values?.get(dim) ?: 0)
            max.add(activeCubes.maxBy { it.values[dim] }?.values?.get(dim) ?: 0)
        }
        spaceBoundaries = Coordinate(min) to Coordinate(max)
    }
}

class ReferenceSystem(coordinates: List<Coordinate>, private var dimensions: Int = 3) {
    val coordinates: List<Coordinate> = coordinates.map {
        val new = it.values.toMutableList()
        while (new.size < dimensions) {
            new.add(0)
        }
        Coordinate(new)
    }

    fun getDimensions(): Int {
        return dimensions
    }
}

fun parseInitialConfigData(fileName: String, dimensions: Int = 3): ReferenceSystem {
    val data = TextFileParser.parseLines(fileName) { it }
    val activeCubes = mutableListOf<Coordinate>()
    data.forEachIndexed { rowIndex, row ->
        row.forEachIndexed { colIndex, cube ->
            if (cube == '#') {
                activeCubes.add(Coordinate(listOf(rowIndex, colIndex)))
            }
        }
    }
    return ReferenceSystem(activeCubes, dimensions)
}
