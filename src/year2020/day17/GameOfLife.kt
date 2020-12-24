package year2020.day17

class GameOfLife(private val referenceSystem: AReferenceSystem, val keepActiveCondition: (Int) -> Boolean, val activateCondition: (Int) -> Boolean) {
    private val activeElements = referenceSystem.coordinates.toMutableSet()

    private val activeCandidates = mutableMapOf<Coordinate, Int>()

    fun getActiveElementsCount(): Int {
        return activeElements.size
    }

    fun skipSteps(steps: Int) {
        repeat(steps) {
            nextStep()
        }
    }

    private fun nextStep() {
        val newActiveElements = mutableSetOf<Coordinate>()
        activeElements.forEach { activeElement ->
            val adjacentActiveElements = findAdjacentActiveElements(activeElement)
            if (keepActiveCondition(adjacentActiveElements)) {
                newActiveElements.add(activeElement)
            }
        }
        val eligibleActiveCandidates = activeCandidates.filterValues { activateCondition(it) }.map { it.key }
        newActiveElements.addAll(eligibleActiveCandidates)
        activeCandidates.clear()
        activeElements.clear()
        activeElements.addAll(newActiveElements)
    }

    private fun findAdjacentActiveElements(activeElement: Coordinate): Int {
        var counter = 0
        referenceSystem.directions.forEach { direction ->
            val targetPosition = activeElement + direction
            if (targetPosition in activeElements) {
                counter++
            } else {
                activeCandidates[targetPosition] = activeCandidates.getOrDefault(targetPosition, 0) + 1
            }
        }
        return counter
    }
}

class Coordinate(val values: List<Int>) {
    operator fun plus(other: Coordinate): Coordinate {
        val newValues = this.values.zip(other.values) { a, b -> a + b }
        return Coordinate(newValues)
    }

    override fun equals(other: Any?): Boolean {
        if (other is Coordinate) {
            return this.values == other.values
        }
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return this.values.hashCode()
    }

    override fun toString(): String {
        return "(" + values.joinToString(", ") + ")"
    }
}

abstract class AReferenceSystem {
    abstract val coordinates: List<Coordinate>
    abstract var directions: List<Coordinate>
    abstract fun getDimensions(): Int
}
