package year2021.day12

import common.TextFileParser

fun main() {
    val input = TextFileParser.parseLines("src/year2021/day12/input.txt") { it }

    println("Day 12 Part 1")
    val caveSystem = Cave.makeFromConnections(input)
    val result1 = caveSystem.findPathsToEnd()
    println(result1)

    println("Day 12 Part 2")
    val result2 = caveSystem.findLongerPathsToEnd()
    println(result2)
}

class Cave(private val name: String, private val connections: MutableSet<Cave> = mutableSetOf()) {

    private fun isSmall() = name.all { it.isLowerCase() }
    private fun isStartPoint() = name == "start"

    fun findPathsToEnd(): Int {
        return navigate("end", shortestTripRule).count()
    }

    fun findLongerPathsToEnd(): Int {
        return navigate("end", longerTripRule).count()
    }

    private fun navigate(
        target: String,
        canVisit: VisitingRule,
        current: Cave = this,
        currentPath: List<Cave> = listOf(current),
        validPaths: MutableSet<List<Cave>> = mutableSetOf()
    ): Set<List<Cave>> {
        current.connections.forEach { destination ->
            if (destination.name == target) {
                validPaths.add(currentPath)
                return@forEach
            }
            if (!canVisit(destination, currentPath)) {
                return@forEach
            }
            navigate(target, canVisit, destination, currentPath + destination, validPaths)
        }
        return validPaths
    }

    private fun addConnection(destination: Cave) {
        connections.add(destination)
    }

    override fun equals(other: Any?): Boolean {
        other as Cave
        return name == other.name
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

    companion object {
        private val allCaves: MutableSet<Cave> = mutableSetOf()

        fun makeFromConnections(connections: List<String>): Cave {
            connections.forEach { connection ->
                makeFromConnection(connection)
            }

            return allCaves.first { it.isStartPoint() }
        }

        private fun makeFromConnection(connection: String) {
            val (startName, endName) = connection.split("-")
            val destination = allCaves.firstOrNull { it.name == endName } ?: Cave(endName)
            val departure = allCaves.firstOrNull { it.name == startName } ?: Cave(startName)
            destination.addConnection(departure)
            departure.addConnection(destination)
            allCaves.addAll(listOf(departure, destination))
        }

        private val shortestTripRule: VisitingRule = { cave, currentPath ->
            !cave.isStartPoint() && !(cave.isSmall() && cave in currentPath)
        }

        private val longerTripRule: VisitingRule = { cave, currentPath ->
            if (cave.isStartPoint()) {
                false
            } else {
                val hasAlreadyVisitedTwiceSmallCave =
                    currentPath.filter { it.isSmall() }.groupingBy { it.name }.eachCount().none { it.value > 1 }
                shortestTripRule(cave, currentPath) || hasAlreadyVisitedTwiceSmallCave
            }
        }

    }
}

typealias VisitingRule = (Cave, List<Cave>) -> Boolean