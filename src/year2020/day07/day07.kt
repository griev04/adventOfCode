package year2020.day07

import common.TextFileParser

fun main() {
    val bags = TextFileParser.parseLines("src/year2020/day07/input.txt") { parseBag(it) }

    println("Part 1")
    println(bags.filter { it.contains(Bag.make("shiny gold")) }.size)

    println("Part 2")
    println(Bag.make("shiny gold").size() - 1)
}

fun parseBag(line: String): Bag {
    val (bag, contentsList) = line.replace("bags", "bag").replace(" bag", "").split(" contain ")
    val contents = contentsList.replace(".", "").split(", ").filter { it != "no other" }
    val innerBags = contents.map {
        val quantity = it.take(1).toInt()
        val b = Bag.make(it.takeLast(it.length - 2))
        InnerBag(quantity, b)
    }
    return Bag.make(bag, innerBags.toSet())
}

class Bag private constructor(val color: String, val content: MutableSet<InnerBag>) {
    // get size by summing recursively its contents.
    // base case: no contents, hence size 1
    fun size(): Int {
        return 1 + content.map { it.quantity * it.bag.size() }.sum()
    }

    // check if any of the inner bags contains the target bag OR if any of its descendant bags contains it (recursively)
    fun contains(other: Bag): Boolean {
        return content.find { it.bag == other || it.bag.contains(other) } != null
    }

    companion object {
        private val bags = mutableListOf<Bag>()

        fun make(color: String, content: Set<InnerBag> = emptySet()): Bag {
            val foundBag = bags.find { it.color == color }
            if (foundBag == null) {
                val newBag = Bag(color, content.toMutableSet())
                bags.add(newBag)
                return newBag
            }
            foundBag.content.addAll(content)
            return foundBag
        }
    }
}

class InnerBag(val quantity: Int, val bag: Bag)
