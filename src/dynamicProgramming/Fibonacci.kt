package dynamicProgramming

import java.math.BigInteger

fun main() {
    println(fib(300))
}

fun fib(n: Int, seen: MutableMap<Int, BigInteger> = mutableMapOf()): BigInteger {
    seen[n]?.also { return it }
    if (n <= 2) return BigInteger.ONE
    val res = fib(n-1, seen) + fib(n-2, seen)
    seen[n] = res
    return res
}