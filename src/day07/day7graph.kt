package day07

import common.TextFileParser

fun main() {
    val graph = parseGraph("src/day7/input.txt")
    val target = "shiny gold"
    println("Part 1")
    val numberOfBagsTypesContainingTarget = graph.findAncestorsCount(target)
    println(numberOfBagsTypesContainingTarget)

    println("Part 2")
    val numberOfBagsContainedInTarget = graph.getChildrenTotalDistance(target)
    println(numberOfBagsContainedInTarget)
}

fun parseGraph(filePath: String): Graph<String> {
    val graph = Graph<String>()
    TextFileParser.parseLines(filePath) { line -> parseGraphNode(line, graph) }
    return graph
}

private fun parseGraphNode(line: String, graph: Graph<String>) {
    val (parentColor, childrenString) = line.replace("bags", "bag").replace(" bag", "").split(" contain ")
    val children = childrenString.replace(".", "").split(", ").filter { it != "no other" }

    val parentNode = graph.addNode(parentColor)
    children.forEach { child ->
        val childNode = graph.addNode(child.takeLast(child.length - 2))
        graph.linkNodes(parentNode, childNode, child.take(1).toInt())
    }
}

class Graph<T> {
    private val nodes: MutableList<Node<T>> = mutableListOf()
    private val nodeAdjacency: MutableMap<Node<T>, MutableMap<Node<T>, Int>> = mutableMapOf()

    fun addNode(newValue: T): Node<T> {
        if (!nodes.map { it.value }.contains(newValue)) {
            val newNode = Node(newValue)
            nodes.add(newNode)
            return newNode
        }
        return findNodeByValue(newValue)!!
    }

    fun linkNodes(startNode: Node<T>, endNode: Node<T>, weight: Int = 1) {
        if (!nodes.containsAll(listOf(startNode, endNode))) {
            return
        }
        // add link between nodes
        nodeAdjacency[startNode] = nodeAdjacency[startNode] ?: mutableMapOf()
        nodeAdjacency[startNode]?.set(endNode, weight)
    }

    fun linkNodes(startValue: T, endValue: T, weight: Int = 1) {
        val startNode = findNodeByValue(startValue)
        val endNode = findNodeByValue(endValue)
        if (startNode != null && endNode != null) {
            linkNodes(startNode, endNode, weight)
        }
    }

    fun findParentNodes(value: T): List<Node<T>> {
        val targetNode = findNodeByValue(value) ?: return emptyList()
        return findParentNodes(targetNode)
    }

    fun findParentNodes(targetNode: Node<T>): List<Node<T>> {
        val parentNodes = nodeAdjacency.filter { outerMapEntry -> outerMapEntry.value.containsKey(targetNode) }
        return parentNodes.keys.toList()
    }

    fun findChildNodes(value: T): List<Node<T>> {
        val targetNode = findNodeByValue(value) ?: return emptyList()
        return findChildNodes(targetNode)
    }

    fun findChildNodes(targetNode: Node<T>): List<Node<T>> {
        val parentNodes = nodeAdjacency[targetNode]?.keys ?: return emptyList()
        return parentNodes.toList()
    }

    fun getChildrenTotalDistance(value: T): Int {
        val targetNode = findNodeByValue(value) ?: return 0
        return getCurrentAndChildrenTotalDistance(targetNode) - 1
    }

    private fun getCurrentAndChildrenTotalDistance(targetNode: Node<T>): Int {
        val children = findChildNodes(targetNode)
        var counter = 1
        children.forEach { child ->
            val distance = getDistance(targetNode, child) ?: 0
            counter += getCurrentAndChildrenTotalDistance(child) * distance
        }
        return counter
    }

    fun getDistance(startNode: Node<T>, endNode: Node<T>): Int? {
        return nodeAdjacency[startNode]?.get(endNode)
    }

    fun findAncestorsCount(value: T): Int {
        val targetNode = findNodeByValue(value) ?: return 0
        return findAncestorsCount(targetNode)
    }

    fun findAncestorsCount(targetNode: Node<T>): Int {
        return findAncestorNodes(targetNode).minus(targetNode).size
    }

    private fun findAncestorNodes(targetNode: Node<T>, visitedNodes: MutableSet<Node<T>> = mutableSetOf()): MutableSet<Node<T>> {
        val parentNodes = findParentNodes(targetNode)
        visitedNodes.add(targetNode)
        if (parentNodes.isNotEmpty()) {
            parentNodes.forEach { findAncestorNodes(it, visitedNodes) }
        }
        return visitedNodes
    }

    private fun findNodeByValue(value: T): Node<T>? {
        return nodes.find { it.value == value }
    }
}

class Node<T>(val value: T)
