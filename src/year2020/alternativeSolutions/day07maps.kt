package year2020.alternativeSolutions

import common.TextFileParser

fun main() {
    val input = TextFileParser.parseLines("src/year2020/day07/input.txt") { line -> parseBags(line) }

    println("Part 1")
    val inverseMap = getInverseMap(input)

    val result1 = findContainers("shiny gold", inverseMap).size
    println(result1)

    println("Part 2")
    val directMap = getDirectMap(input)
    val result2 = findInnerBags(Sack("shiny gold"), directMap) - 1
    println(result2)
}

private fun parseBags(line: String): Sack {
    val (parent, childrenString) = line.replace("bags", "bag").replace(" bag", "").split(" contain ")
    val children = childrenString.replace(".", "").split(", ").filter { it != "no other" }
    return Sack(parent, inner = children.map { Sack(it.takeLast(it.length - 2), it.take(1).toInt()) })
}

private fun getDirectMap(sacks: List<Sack>): MutableMap<String, MutableList<Sack>> {
    val directMap = mutableMapOf<String, MutableList<Sack>>()
    sacks.forEach { bag ->
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

private fun getInverseMap(sacks: List<Sack>): MutableMap<String, MutableList<String>> {
    val inverseMap = mutableMapOf<String, MutableList<String>>()
    sacks.forEach { bag ->
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

fun findInnerBags(sack: Sack, map: Map<String, List<Sack>>): Int {
    val children = map[sack.color] ?: return sack.quantity
    if (children.isEmpty()) return sack.quantity
    var result = sack.quantity
    children.forEach { child ->
        result += sack.quantity * findInnerBags(child, map)
    }
    return result
}

class Sack(val color: String, val quantity: Int = 1, val inner: List<Sack> = emptyList())
