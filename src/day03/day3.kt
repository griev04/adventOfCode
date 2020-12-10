package day03

import common.TextFileParser

fun main() {
    val inputFile = "src/day3/input.txt"
    println("Naive solution")
    val input = TextFileParser.parseLines(inputFile) {it}
    println(move(input, 3, 1))
    val slopes = listOf(Pair(1, 1), Pair(3, 1), Pair(5, 1), Pair(7, 1), Pair(1, 2))
    val result = slopes.fold(1L) { acc, navigationPattern ->
        val numberOfTreesOfSlope = move(input, navigationPattern.first, navigationPattern.second)
        acc * numberOfTreesOfSlope
    }
    println(result)

//    // Overcomplicated and engineered solutions
//    println("Solution with lists")
//    val contourLines = TextFileParser.parseLines(inputFile) { row ->
//        parseTreesToList(row)
//    }
//    val topography = Topography(contourLines)
//    compute(topography)
//
//    println("Solution with maps")
//    val treesMap = TextFileParser.parseAsIndexedMap(inputFile) { row ->
//        parseTreesToMap(row)
//    }
//    val gridSize = TextFileParser.getSizeOfGrid(inputFile)
//    val topography2 = TopographyWithMaps(treesMap, Position(gridSize.first, gridSize.second))
//    compute(topography2)
}

fun move(pattern: List<String>, dx: Int, dy: Int): Long {
    var column = 0
    var counter = 0
    for (row in pattern.indices step dy) {
        if (pattern[row][column] == "#".first()) {
            counter++
        }
        column = (column + dx) % pattern[0].length
    }
    return counter.toLong()
}

//private fun compute(topography: ITopography) {
//    println("Part 1")
//    val navigationSystem = Navigation(topography)
//    val numberOfTrees = navigationSystem.setNavigationPattern(Position(3, 1)).descend().getNumberOfTrees()
//    println(numberOfTrees)
//    // Part 2
//    println("Part 2")
//    val slopes = listOf(Position(1, 1), Position(3, 1), Position(5, 1), Position(7, 1), Position(1, 2))
//    val result = slopes.fold(1L) { acc, navigationPattern ->
//        val numberOfTreesOfSlope = navigationSystem.setNavigationPattern(navigationPattern).descend().getNumberOfTrees()
//        acc * numberOfTreesOfSlope
//    }
//    println(result)
//}
//
//fun parseTreesToList(row: String): TopographyRow {
//    val treesList = row.map { it == "#".first() }
//    return TopographyRow(treesList)
//}
//
//fun parseTreesToMap(row: String): Map<Int, Boolean> {
//    val map = mutableMapOf<Int, Boolean>()
//    row.forEachIndexed { index, character ->
//        if (character == "#".first()) {
//            map[index] = true
//        }
//    }
//    return map
//}
//
//class Navigation(private val topography: ITopography) {
//    private var navigationPattern: Position = Position(0, 0)
//    private var currentPosition: Position = Position(0, 0)
//    private var isMoving: Boolean = false
//    private var totalDescent: Int = 0
//    private var numberOfTrees: Int = 0
//
//    private val size = topography.getSize()
//
//    private fun isCurrentPositionWithTree(): Boolean {
//        return topography.isTreeAtPosition(currentPosition)
//    }
//
//    private fun move() {
//        totalDescent += navigationPattern.y
//        if (totalDescent < size.y) {
//            val y = (currentPosition.y + navigationPattern.y) % size.y
//            val x = (currentPosition.x + navigationPattern.x) % size.x
//            currentPosition = Position(x, y)
//        } else {
//            isMoving = false
//        }
//    }
//
//    fun setNavigationPattern(navigationPattern: Position): Navigation {
//        this.navigationPattern = navigationPattern
//        this.reset()
//        return this
//    }
//
//    fun descend(): Navigation {
//        isMoving = true
//        while (isMoving) {
//            if (isCurrentPositionWithTree()) {
//                numberOfTrees += 1
//            }
//            move()
//        }
//        return this
//    }
//
//    fun getNumberOfTrees(): Int {
//        return numberOfTrees
//    }
//
//    private fun reset() {
//        this.currentPosition = Position(0, 0)
//        numberOfTrees = 0
//        totalDescent = 0
//        isMoving = false
//    }
//}
//
//class Topography(private val topographyRows: List<TopographyRow>) : ITopography {
//    override fun getSize(): Position {
//        return Position(topographyRows[0].getSize(), topographyRows.size)
//    }
//
//    override fun isTreeAtPosition(position: Position): Boolean {
//        return topographyRows[position.y].get(position.x)
//    }
//}
//
//class TopographyWithMaps(private val treesMap: Map<Int, Map<Int, Boolean>>, private val gridSize: Position) : ITopography {
//    override fun getSize(): Position {
//        return gridSize
//    }
//
//    override fun isTreeAtPosition(position: Position): Boolean {
//        return treesMap[position.y]?.get(position.x) ?: false
//    }
//}
//
//interface ITopography {
//    fun getSize(): Position
//    fun isTreeAtPosition(position: Position): Boolean
//}
//
//class TopographyRow(private val treePositions: List<Boolean>) {
//    fun getSize(): Int {
//        return treePositions.size
//    }
//
//    fun get(x: Int): Boolean {
//        return treePositions[x]
//    }
//}
//
//class Position(val x: Int, val y: Int)