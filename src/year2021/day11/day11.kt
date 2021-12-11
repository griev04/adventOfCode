package year2021.day11

import common.TextFileParser

fun main() {
    val input = TextFileParser.parseLines("src/year2021/day11/input.txt") { row ->
        row.split("").filter { it.isNotEmpty() }.map { it.toInt() }
    }

    println("Day 11 Part 1")
    val map = BrightnessMap.makeFromMatrix(input).run()
    val result1 = map.getTotalFlashes()
    println(result1)

    println("Day 11 Part 2")
    val result2 = map.getFirstSimultaneousFlashStep()
    println(result2)
}

class BrightnessMap private constructor(
    private val octopusByPosition: Map<Position, Octopus>,
    private val mapSize: Pair<Int, Int>
) {
    private var totalFlashes = 0
    private var firstSimultaneousFlashStep: Int? = null

    fun run(): BrightnessMap {
        repeat(1000) {
            if (firstSimultaneousFlashStep == null) {
                step(it)
            }
        }
        return this
    }

    fun getTotalFlashes() = totalFlashes

    fun getFirstSimultaneousFlashStep() = firstSimultaneousFlashStep

    private fun step(step: Int) {
        val flashed = octopusByPosition.filter {
            it.value.increaseEnergyLevel().hasFlashed()
        }

        flashAdjacent(flashed)
        val stepFlashes =
            octopusByPosition.filter { it.value.hasFlashed() }.map { it.value.resetAfterFlash() }.size.also {
                if (firstSimultaneousFlashStep == null && it == octopusByPosition.size) {
                    firstSimultaneousFlashStep = step + 1
                }
            }
        if (step < 100) {
            totalFlashes += stepFlashes
        }

    }

    private fun flashAdjacent(flashed: Map<Position, Octopus>) {
        val newFlashes = mutableMapOf<Position, Octopus>()
        flashed.forEach {
            val (position) = it
            (-1..1).forEach { x ->
                (-1..1).forEach { y ->
                    val newPosition = Position(position.x + x, position.y + y)

                    if ((x != 0 || y != 0) && newPosition.inMap(mapSize)) {
                        octopusByPosition[newPosition]!!.increaseEnergyLevel().also { octopus ->
                            if (octopus.hasJustFlashed()) {
                                newFlashes[newPosition] = octopus
                            }
                        }
                    }
                }
            }
        }
        if (newFlashes.isNotEmpty()) {
            flashAdjacent(newFlashes)
        }
    }

    companion object {
        fun makeFromMatrix(input: List<List<Int>>): BrightnessMap {
            val result = mutableMapOf<Position, Octopus>()
            input.forEachIndexed { rowIndex, row ->
                row.forEachIndexed { colIndex, value ->
                    result[Position(colIndex, rowIndex)] = Octopus(value)
                }
            }
            val mapSize = input.first().size to input.size
            return BrightnessMap(result, mapSize)
        }
    }
}

data class Position(val x: Int, val y: Int) {
    fun inMap(mapSize: Pair<Int, Int>): Boolean {
        return x in (0 until mapSize.first) && y in (0 until mapSize.second)
    }

    override fun equals(other: Any?): Boolean {
        other as Position
        return other.x == x && other.y == y
    }

    override fun hashCode(): Int {
        return x.hashCode() + 100 * y.hashCode()
    }

    override fun toString(): String {
        return "$x, $y"
    }
}

data class Octopus(private var brightness: Int) {
    private var flashed: Boolean = true

    fun increaseEnergyLevel(): Octopus {
        brightness += 1
        flashed = brightness > 9
        return this
    }

    fun hasJustFlashed() = flashed && brightness == 10

    fun hasFlashed(): Boolean = flashed

    fun resetAfterFlash(): Octopus {
        if (brightness > 9) {
            flashed = false
            brightness = 0
        }
        return this
    }

    override fun toString(): String {
        return brightness.toString()
    }
}