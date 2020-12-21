package year2020.day21

import common.TextFileParser

fun main() {
    val labels = TextFileParser.parseLines("src/year2020/day21/input.txt") { parse(it) }
    val analyzer = LabelAnalyzer(labels)
    println("Part 1")
    val safeIngredientsCount = analyzer.countSafeIngredientsOccurrence()
    println(safeIngredientsCount)

    println("Part 2")
    val dangerousList = analyzer.getDangerousList()
    println(dangerousList)
}

class LabelAnalyzer(private val labels: List<Label>) {
    private lateinit var allergens: Set<String>
    private lateinit var ingredients: Set<String>
    private lateinit var safeIngredients: Set<String>
    private val allergensIngredients = mutableMapOf<String, MutableSet<String>>()

    init {
        getAllergensList()
        getIngredientsList()
        findSafeIngredients()
    }

    fun getDangerousList(): String {
        return allergensIngredients.entries.sortedBy { it.key }.joinToString(",") { it.value.first() }
    }

    fun countSafeIngredientsOccurrence(): Int {
        return safeIngredients.fold(0) { acc, ingredient ->
            acc + labels.map { it.ingredients }.flatten().count { it == ingredient }
        }
    }

    private fun findSafeIngredients() {
        // define possible allergenic ingredients
        allergens.forEach { allergen ->
            val correspondingLabels = labels.filter { it.allergens.contains(allergen) }
            val correspondingLabelsIngredients = correspondingLabels.map { label -> label.ingredients.toSet() }.reduce { acc, set -> acc.intersect(set) }
            allergensIngredients[allergen] = correspondingLabelsIngredients.toMutableSet()
        }

        // narrow down to specific allergenic ingredient
        while (allergensIngredients.count { it.value.size > 1 } > 0) {
            val allergenMatches = allergensIngredients.entries.filter { it.value.size == 1 }.map { it.value.first() }
            allergenMatches.forEach { match ->
                allergensIngredients.filter { it.value.size > 1 }.forEach { (_, v) ->
                    v.remove(match)
                }
            }
        }

        val unsafeIngredients = allergensIngredients.values.flatten().toSet()
        safeIngredients = ingredients.toMutableSet().minus(unsafeIngredients)
    }

    private fun getAllergensList() {
        allergens = labels.map { it.allergens }.flatten().toSet()
    }

    private fun getIngredientsList() {
        ingredients = labels.map { it.ingredients }.flatten().toSet()
    }
}

fun parse(line: String): Label {
    val (ingredientsText, allergensText) = line.split(" (contains ")
    val ingredients = ingredientsText.split(" ")
    val allergens = allergensText.replace(", ", " ").replace(")", "").split(" ")
    return Label(ingredients, allergens)
}

class Label(val ingredients: List<String>, val allergens: List<String>)
