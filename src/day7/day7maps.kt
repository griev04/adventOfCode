package day7

import common.TextFileParser

fun main() {
    val input = TextFileParser.parseLines("src/day7/input.txt") { line -> parseBags(line) }

    println("Part 1")
    val inverseMap = getInverseMap(input)

    val result1 = findContainers("shiny gold", inverseMap).size
    println(result1)

    println("Part 2")
    val directMap = getDirectMap(input)
    val result2 = findInnerBags(Bag("shiny gold"), directMap) - 1
    println(result2)
}

private fun parseBags(line: String): Bag {
    val (parent, childrenString) = line.replace("bags", "bag").replace(" bag", "").split(" contain ")
    val children = childrenString.replace(".", "").split(", ").filter { it != "no other" }
    return Bag(parent, inner = children.map { Bag(it.takeLast(it.length - 2), it.take(1).toInt()) })
}

private fun getDirectMap(bags: List<Bag>): MutableMap<String, MutableList<Bag>> {
    val directMap = mutableMapOf<String, MutableList<Bag>>()
    bags.forEach { bag ->
        val parentColor = bag.color
        val children = bag.inner
        if (directMap[parentColor] != null) {
            directMap[parentColor]?.addAll(children)
        } else {
            directMap[parentColor] = children.toMutableList()
        }
    }
    return directMap
}

private fun getInverseMap(bags: List<Bag>): MutableMap<String, MutableList<String>> {
    val inverseMap = mutableMapOf<String, MutableList<String>>()
    bags.forEach { bag ->
        val parentColor = bag.color
        val children = bag.inner

        children.forEach { child ->
            if (inverseMap[child.color] != null) {
                inverseMap[child.color]?.add(parentColor)
            } else {
                inverseMap[child.color] = mutableListOf(parentColor)
            }
        }
    }
    return inverseMap
}

fun findContainers(bag: String, map: Map<String, List<String>>, result: MutableList<String> = mutableListOf()): MutableList<String> {
    val parents = map[bag] ?: return result
    result.addAll(parents)
    parents.forEach { parent ->
        findContainers(parent, map, result)
    }
    return result.distinct().toMutableList()
}

fun findInnerBags(bag: Bag, map: Map<String, List<Bag>>): Int {
    val children = map[bag.color] ?: return bag.quantity
    if (children.isEmpty()) return bag.quantity
    var result = bag.quantity
    children.forEach { child ->
        result += bag.quantity * findInnerBags(child, map)
    }
    return result
}

class Bag(val color: String, val quantity: Int = 1, val inner: List<Bag> = emptyList())
