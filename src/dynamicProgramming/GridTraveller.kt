package dynamicProgramming

import java.math.BigInteger

// you can move right or down in the grid to reach the bottom left corner

fun main() {
    val grid = GridTraveller(3, 4)
    val result = grid.countWaysToTravel()
    println(result)
    val result2 = gridTraveller(3, 4)
    println(result2)
}

// reduce grid size at each iteration until base case of 1x0 or 0x1
fun gridTraveller(rows: Int, cols: Int, seen: MutableMap<Pair<Int, Int>, BigInteger> = mutableMapOf()): BigInteger {
    seen[Pair(rows, cols)]?.also { return it }
    if (rows == 0 || cols == 0) return BigInteger.ZERO
    if (rows == 1 || cols == 1) return BigInteger.ONE

    var sum = BigInteger.ZERO

    // move Right
    sum += gridTraveller(rows - 1, cols)

    // move Down
    sum += gridTraveller(rows, cols - 1)

    seen[Pair(rows, cols)] = sum
    return sum
}

// walk through grid chosing different directions
class GridTraveller(private val rows: Int, private val cols: Int) {
    private var size: Int = rows * cols

    fun countWaysToTravel(pos: Int = 0, seen: MutableMap<Int, BigInteger> = mutableMapOf()): BigInteger {
        seen[pos]?.also { return it }
        if (pos == size - 1) {
            return BigInteger.ONE
        }
        var sum = BigInteger.ZERO
        // move right
        if (pos % cols < cols - 1) {
            sum += countWaysToTravel(pos + 1, seen)
        }
        // move down
        if (pos / rows < rows) {
            sum += countWaysToTravel(pos + cols, seen)
        }
        seen[pos] = sum
        return sum
    }

}