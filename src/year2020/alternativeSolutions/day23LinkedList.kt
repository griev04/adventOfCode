package year2020.alternativeSolutions

import common.TextFileParser

fun main() {
    val cups = TextFileParser.parseFile("src/year2020/day23/input.txt") { parseCups(it) }

    println("Part 1")
    val game = CircularListGame(cups)
    val res1 = game.playVariant().getResult()
    println(res1)

    println("Part 2")
    val newCups = updateCupsUpTo(cups, 1000000)
    val game2 = CircularListGame(newCups)
    val res2 = game2.playVariant(true).getResult()
    println(res2)
}

class CircularListGame(input: List<Int>) {
    private lateinit var valuesNodeMap: Map<Int, LinkedNode>
    private lateinit var currentNode: LinkedNode
    private var minimum: Int = 0
    private var maximum: Int = 0
    private var isVariant: Boolean = false

    init {
        initCircularDataStructure(input)
    }

    fun playVariant(isVariant: Boolean = false): CircularListGame {
        this.isVariant = isVariant
        val moves = if (isVariant) {
            10000000
        } else {
            100
        }
        repeat(moves) {
            playMove()
        }
        return this
    }

    fun getResult(): String {
        return if (isVariant) {
            val resultList = getCurrentList(1, 2)
            (resultList[1].toBigInteger() * resultList[2].toBigInteger()).toString()
        } else {
            val resultList = getCurrentList(1)
            resultList.joinToString("")
        }
    }

    private fun getCurrentList(startingValue: Int = -1, requiredLength: Int = valuesNodeMap.size): List<Int> {
        val startNode = valuesNodeMap[startingValue] ?: currentNode
        val result = mutableListOf(startNode.value)
        var currentNode = startNode.next
        while (currentNode != startNode && result.size < requiredLength + 1) {
            result.add(currentNode.value)
            currentNode = currentNode.next
        }
        return result
    }

    private fun playMove() {
        // find next 3
        val firstOfThree = currentNode.next
        val secondOfThree = firstOfThree.next
        val thirdOfThree = secondOfThree.next
        // link current to the following node -> detach 3 nodes
        currentNode.linkTo(thirdOfThree.next)
        // find destination node
        val destinationNode = findDestination(listOf(firstOfThree.value, secondOfThree.value, thirdOfThree.value))
        // link last removed to node following the destination
        thirdOfThree.linkTo(destinationNode.next)
        // link destination node to first removed node
        destinationNode.linkTo(firstOfThree)
        nextNode()
    }

    private fun findDestination(removedValues: List<Int>): LinkedNode {
        var targetValue = currentNode.value - 1
        while (true) {
            when {
                (targetValue in removedValues) -> targetValue--
                (targetValue < minimum) -> targetValue = maximum
                else -> valuesNodeMap[targetValue]?.let { return it }
            }
        }
    }

    private fun nextNode() {
        currentNode = currentNode.next
    }

    private fun initCircularDataStructure(input: List<Int>) {
        var previous: LinkedNode? = null
        valuesNodeMap = input.map { value ->
            val current = LinkedNode(value)
            previous?.linkTo(current)
            previous = current
            value to current
        }.toMap()
        // link last element to first one to have circular list
        currentNode = valuesNodeMap[input[0]]!!
        currentNode.let { previous?.linkTo(it) }
        minimum = input.min() ?: 0
        maximum = input.max() ?: 0
    }
}

class LinkedNode(val value: Int) {
    lateinit var next: LinkedNode
    fun linkTo(other: LinkedNode) {
        next = other
    }
}

fun updateCupsUpTo(cups: List<Int>, max: Int): List<Int> {
    val newCups = cups.toMutableList()
    newCups.addAll((newCups.max()!! + 1)..max)
    return newCups
}

fun parseCups(text: String): List<Int> {
    return text.split("").filter { it.isNotEmpty() && it != "\n" }.map { it.toInt() }
}
