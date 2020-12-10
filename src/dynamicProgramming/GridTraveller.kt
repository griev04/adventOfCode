package dynamicProgramming

import java.math.BigInteger

fun main() {
    val grid = GridTraveller(100, 10)
    val result = grid.countWaysToTravel()
    println(result)
}

class GridTraveller(private val rows: Int, private val cols: Int) {
    private var size: Int = rows * cols

    // you can move right or down in the grid to reach the bottom left corner
    fun countWaysToTravel(pos: Int = 0, seen: MutableMap<Int, BigInteger> = mutableMapOf()): BigInteger {
        seen[pos]?.also { return it }
        if (pos == size - 1) {
            return BigInteger.ZERO
        }
        var sum = BigInteger.ZERO
        // move right
        if (pos / cols < cols - 1) {
            sum += BigInteger.ONE + countWaysToTravel(pos + 1, seen)
        }
        // move down
        if (pos / rows < rows - 1) {
            sum += BigInteger.ONE + countWaysToTravel(pos + 1, seen)
        }
        seen[pos] = sum
        return sum
    }

}