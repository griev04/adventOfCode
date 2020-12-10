package dynamicProgramming

fun main() {
    println(bestSum(7, listOf(5, 3, 4, 7)))
    println(bestSum(8, listOf(2, 3, 5)))
    println(bestSum(8, listOf(1, 4, 5)))
    println(bestSum(100, listOf(1, 2, 5, 25)))
}

fun bestSum(targetSum: Int, numbers: List<Int>, memo: MutableMap<Int, List<Int>?> = mutableMapOf()): List<Int>? {
    memo[targetSum]?.also { return it }
    if (targetSum == 0) return listOf()
    if (targetSum < 0) return null
    memo[targetSum] = numbers.mapNotNull {
        var res = bestSum(targetSum - it, numbers, memo)
        if (res != null) {
            res = res.toMutableList()
            res.add(it)
        }
        res
    }.minBy { it.size }
    return memo[targetSum]
}