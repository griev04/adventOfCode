package year2020.day18

import common.TextFileParser

fun main() {
    val input = TextFileParser.parseLines("src/year2020/day18/input.txt") { parse(it) }
    println("Day 18 Part 1")
    val result = input.map { evaluateExpression(it) { a -> toRpn(a)} }.sum()
    println(result)

    println("Day 18 Part 2")
    val result2 = input.map { evaluateExpression(it) { a -> toRpnPart2(a) } }.sum()
    println(result2)
}

fun evaluateExpression(expression: Expression, rpn: (Expression) -> Expression): Long {
    val openParenthesis = mutableListOf<Int>()
    val tokenList = expression.tokens.toMutableList()
    var pos = 0
    while (pos < tokenList.size) {
        val char = tokenList[pos]
        if (char == "(") {
            openParenthesis.add(pos)
        }
        if (char == ")") {
            val openedAt = openParenthesis.last()
            openParenthesis.removeAt(openParenthesis.size - 1)
            val subExpression = Expression(tokenList.subList(openedAt + 1, pos))
            val result = evaluateRpnWithStack(rpn(subExpression))
            repeat(pos+1-openedAt) {
                tokenList.removeAt(openedAt)
            }
            tokenList.add(openedAt, result.toString())
            pos = openedAt
        }
        pos++
    }
    return evaluateRpnWithStack(rpn(Expression(tokenList)))
}

fun toRpn(expression: Expression): Expression {
    val values = expression.tokens.filter { !"+-*/".contains(it) }.reversed()
    val symbols = expression.tokens.filter { "+-*/".contains(it) }
    return Expression(listOf(values, symbols).flatten())
}

fun toRpnPart2(expression: Expression): Expression {
    val terms = expression.tokens.toMutableList()
    var length = terms.size
    var pos = 0
    while (pos < length) {
        when (val curr = terms[pos]) {
            "+", "-" -> {
                terms[pos] = terms[pos+1]
                terms[pos+1] = curr
                pos += 2
            }
            "*", "/" -> {
                terms.removeAt(pos)
                terms.add(curr)
                length--
            }
            else -> {
                pos++
            }
        }
    }
    return Expression(terms)
}

fun evaluateRpnWithStack(expression: Expression): Long {
    val stack = Stack<Long>()
    expression.tokens.forEach { value ->
        val number = value.toLongOrNull()
        if (number != null) {
            stack.add(number)
        } else {
            val a = stack.pop()
            val b = stack.pop()
            val res = when (value) {
                "+" -> a + b
                "-" -> a - b
                "*" -> a * b
                "/" -> a / b
                else -> 0
            }
            stack.add(res)
        }
    }
    return stack.pop()
}

fun parse(line: String): Expression {
    val tokens = line.replace("(", "( ").replace(")", " )").split(" ")
    return Expression(tokens)
}

class Expression(val tokens: List<String>)

class Stack<T> {
    private val list = mutableListOf<T>()

    fun add(el: T) {
        list.add(0, el)
    }

    fun pop(): T {
        return list.removeAt(0)
    }
}
