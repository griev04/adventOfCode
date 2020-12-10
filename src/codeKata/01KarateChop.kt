package codeKata

import kotlin.math.floor

fun main() {
    println("-1, ${chop(3, listOf())}")
    println("-1, ${chop(3, listOf(1))}")
    println("0,  ${chop(1, listOf(1))}")
    println("0,  ${chop(1, listOf(1, 3, 5))}")
    println("1,  ${chop(3, listOf(1, 3, 5))}")
    println("2,  ${chop(5, listOf(1, 3, 5))}")
    println("-1, ${chop(0, listOf(1, 3, 5))}")
    println("-1, ${chop(2, listOf(1, 3, 5))}")
    println("-1, ${chop(4, listOf(1, 3, 5))}")
    println("-1, ${chop(6, listOf(1, 3, 5))}")
    println("0,  ${chop(1, listOf(1, 3, 5, 7))}")
    println("1,  ${chop(3, listOf(1, 3, 5, 7))}")
    println("2,  ${chop(5, listOf(1, 3, 5, 7))}")
    println("3,  ${chop(7, listOf(1, 3, 5, 7))}")
    println("-1, ${chop(0, listOf(1, 3, 5, 7))}")
    println("-1, ${chop(2, listOf(1, 3, 5, 7))}")
    println("-1, ${chop(4, listOf(1, 3, 5, 7))}")
    println("-1, ${chop(6, listOf(1, 3, 5, 7))}")
    println("-1, ${chop(8, listOf(1, 3, 5, 7))}")
}

fun chop(term: Int, list: List<Int>): Int {
    return recursiveProperChop(term, list)
}

fun recursiveProperChop(term: Int, list: List<Int>, lower: Int = 0, upper: Int = list.size - 1): Int {
    val mid = lower + (upper - lower)/2

    if (upper >= lower) {
        if (list[mid] == term) {
            return mid
        }

        if (list[mid] > term) {
            return recursiveProperChop(term, list, lower = lower, upper = mid - 1)
        }

        if (list[mid] < term) {
            return recursiveProperChop(term, list, lower = mid + 1, upper = upper)
        }
    }
    return - 1
}

fun recursiveChop(term: Int, list: List<Int>, offset: Int = 0): Int {
    val listSize = list.size
    val middlePosition = floor(listSize.toDouble() / 2).toInt()

    if (list.isEmpty() || (listSize == 1 && list[0] != term)) {
        return -1
    }

    if (list[middlePosition] == term) {
        return middlePosition
    }

    if (list[middlePosition] > term) {
        val newList = list.subList(0, middlePosition)
        return recursiveChop(term, newList, offset)
    }

    val newOffset = middlePosition + 1
    val newList = list.subList(offset, listSize)
    val result = recursiveChop(term, newList, newOffset)
    return if (result == -1) {
        result
    } else {
        offset + result
    }
}

fun iterativeChop(term: Int, list: List<Int>): Int {
    var result = -1
    var currentOffset = 0
    var currentList = list

    while (currentList.isNotEmpty() && result == -1) {
        if (currentList.size == 1 && currentList[0] == term) {
            result = currentOffset
        }
        val middlePosition = floor(currentList.size.toDouble() / 2).toInt()
        if (currentList[middlePosition] == term) {
            result = currentOffset + middlePosition
        }
        if (currentList[middlePosition] > term) {
            currentList = currentList.subList(0, middlePosition)
        } else {
            val offset = middlePosition + 1
            currentList = currentList.subList(offset, currentList.size)
            currentOffset += offset
        }
    }
    return result
}

fun iterativeWithBoundsChop(term: Int, list: List<Int>): Int {
    var lowerBound = 0
    var upperBound = list.size - 1

    while (upperBound >= lowerBound) {
        if (upperBound - lowerBound < 0) {
            return -1
        }
        val middlePosition = lowerBound + floor((upperBound - lowerBound).toDouble() / 2).toInt()

        if (list[middlePosition] == term) {
            return middlePosition
        }
        if (list[middlePosition] > term) {
            upperBound = middlePosition - 1
        }
        if (list[middlePosition] < term) {
            lowerBound = middlePosition + 1
        }
    }
    return -1
}