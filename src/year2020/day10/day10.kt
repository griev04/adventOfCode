package year2020.day10

import common.TextFileParser

fun main() {
    val data = TextFileParser.parseLines("src/year2020/day10/input.txt") { it.toInt() }
    println("Day 10 Part 1")
    println(findJoltDifferencesProduct(data))
    println(findJoltDifferencesProductWithMap(data))

    println("Day 10 Part 2")
    val tree = parseTree(data)
    val treeBranches = tree.countBranches()
    println(treeBranches)
}

fun parseTree(list: List<Int>): Node {
    val sortedList = list.sorted()
    var l = -1
    var u = 0

    var curr = 0
    var next = sortedList[0]
    val children = mutableListOf<Int>()

    val tree = Node.make(curr)

    while (u > l) {
        while (next - curr <= 3) {
            children.add(next)
            u++
            if (u >= sortedList.size) break
            next = sortedList[u]
        }
        Node.make(curr, children)
        l++
        u = l + 1
        curr = sortedList[l]
        if (u >= sortedList.size) break
        next = sortedList[u]
        children.clear()
    }

    return tree
}

class Node private constructor(val value: Int, children: List<Node>) {
    private val childNodes = mutableSetOf<Node>()

    init {
        childNodes.addAll(children)
    }

    private fun addChildren(childNodes: List<Node>): Node {
        this.childNodes.addAll(childNodes)
        return this
    }

    // use map to track seen nodes so that it doesn't have to walk the tree again to get known rules
    fun countBranches(seen: MutableMap<Int, Long> = mutableMapOf()): Long {
        if (this.value in seen) return seen[this.value]!!
        // base case: return 1 if end of the branch
        if (childNodes.size == 0) {
            seen[this.value] = 1L
            return 1L
        }
        // Count branches from this node
        val count = childNodes.map { it.countBranches(seen) }.sum()
        seen[this.value] = count
        return count
    }

    fun findTreeHeight(seen: MutableMap<Int, Long> = mutableMapOf()): Long {
        if (this.value in seen) return seen[this.value]!!
        val count = 1 + (childNodes.map { it.findTreeHeight(seen) }.max() ?: 0)
        seen[this.value] = count
        return count
    }

    companion object {
        private val nodes: MutableSet<Node> = mutableSetOf()
        fun make(value: Int, childrenValues: List<Int> = emptyList()): Node {
            // create child nodes if not there
            val childNodes = childrenValues.map { child -> make(child) }
            val foundNode = nodes.find { it.value == value }
            return if (foundNode == null) {
                val newNode = Node(value, childNodes)
                nodes.add(newNode)
                newNode
            } else {
                foundNode.addChildren(childNodes)
            }
        }
    }
}

fun findJoltDifferencesProduct(list: List<Int>): Int {
    val sortedList = list.sorted()

    var counter1 = 0
    var counter3 = 0

    for (i in sortedList.indices) {
        if (i == 0) {
            if (sortedList[0] == 1) counter1++
            if (sortedList[0] == 3) counter3++
        } else {
            val diff = sortedList[i] - sortedList[i - 1]
            if (diff == 1) counter1++
            if (diff == 3) counter3++
        }
    }

    counter3++
    return counter1 * counter3
}

fun findJoltDifferencesProductWithMap(list: List<Int>): Int {
    val sortedList = list.sorted()
    val counters = mutableMapOf(1 to 0, 3 to 1)

    counters.merge(sortedList[0], 1) {
        old, value -> old + value
    }

    var difference: Int
    for (i in 1 until sortedList.size) {
        difference = sortedList[i] - sortedList[i - 1]
        counters.merge(difference, 1) {
            old, value -> old + value
        }
    }
    return counters.values.reduce { acc, i -> acc * i }
}
