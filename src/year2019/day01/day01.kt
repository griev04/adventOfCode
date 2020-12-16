package year2019.day01

import common.TextFileParser

fun main() {
    val modules = TextFileParser.parseLines("src/year2019/year2020.day01/input.txt") { it.toInt() }
    println("Part 1")
    val requiredModulesFuel = calculateNeededFuel(modules) { calculateSimpleFuel(it) }
    val requiredFuel = requiredModulesFuel.sum()
    println(requiredFuel)
    println("Part 2")
    val additionalModulesFuel = calculateNeededFuel(requiredModulesFuel) { calculateFuelForWeight(it) }
    val additionalFuel = additionalModulesFuel.sum()
    val totalFuel = requiredFuel + additionalFuel
    println(totalFuel)
}

fun calculateNeededFuel(modules: List<Int>, rule: (Int) -> Int): List<Int> {
    return modules.map { rule(it) }
}

fun calculateSimpleFuel(weight: Int): Int {
    return weight / 3 - 2
}

fun calculateFuelForWeight(weight: Int): Int {
    if (weight <= 0) return 0
    val req = weight / 3 - 2
    if (req <= 0) return 0
    return req + calculateFuelForWeight(req)
}