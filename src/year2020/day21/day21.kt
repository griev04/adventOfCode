package year2020.day21

import common.TextFileParser

fun main() {
    val labels = TextFileParser.parseLines("src/year2020/day21/input.txt") { parse(it) }
    val analyzer = LabelAnalyzer(labels)
    println("Day21 Part 1")
    val safeIngredientsCount = analyzer.countSafeIngredientsOccurrence()
    println(safeIngredientsCount)

    println("Day21 Part 2")
    val dangerousList = analyzer.getDangerousList()
    println(dangerousList)
}

class LabelAnalyzer(private val labels: List<Label>) {
    private val ingredients = labels.map { it.ingredients }.flatten().toSet()
    private val allergens = labels.map { it.allergens }.flatten().toSet()
    private val safeIngredients = mutableSetOf<String>()
    private val allergensIngredients = mutableMapOf<String, MutableSet<String>>()

    init {
        matchAllergensToIngredients()
    }

    fun getDangerousList(): String {
        return allergensIngredients.entries.sortedBy { it.key }.joinToString(",") { it.value.first() }
    }

    fun countSafeIngredientsOccurrence(): Int {
        return safeIngredients.map { ingredient ->
            labels.map { it.ingredients }.flatten().count{ it == ingredient }
        }.sum()
    }

    private fun matchAllergensToIngredients() {
        // define possible allergenic ingredients
        allergens.forEach { allergen ->
            val correspondingLabelsIngredients = labels.filter { it.allergens.contains(allergen) }.map { label -> label.ingredients.toSet() }
            val commonIngredients = correspondingLabelsIngredients.reduce { acc, set -> acc.intersect(set) }
            allergensIngredients[allergen] = commonIngredients.toMutableSet()
        }

        // narrow down to specific allergenic ingredient
        while (allergensIngredients.count { it.value.size > 1 } > 0) {
            val allergenMatches = allergensIngredients.entries.filter { it.value.size == 1 }.map { it.value.first() }
            allergenMatches.forEach { match ->
                allergensIngredients.filter { it.value.size > 1 }.forEach { (_, ingredients) ->
                    ingredients.remove(match)
                }
            }
        }

        val unsafeIngredients = allergensIngredients.values.flatten().toSet()
        safeIngredients.addAll(ingredients.toMutableSet().minus(unsafeIngredients))
    }
}

fun parse(line: String): Label {
    val (ingredientsText, allergensText) = line.split(" (contains ")
    val ingredients = ingredientsText.split(" ")
    val allergens = allergensText.replace(", ", " ").replace(")", "").split(" ")
    return Label(ingredients, allergens)
}

class Label(val ingredients: List<String>, val allergens: List<String>)
