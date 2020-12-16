package year2020.day05

import common.TextFileParser
import kotlin.math.pow

fun main() {
    val seats = TextFileParser.parseLines("src/day5/input.txt") { parseSeat(it) }

    println("Part 1")
    val maxSeatId = seats.maxBy { it.seatId }?.seatId
    println(maxSeatId)

    println("Part 2")
    val missingSeatId = findMissingSeatId(seats)
    println(missingSeatId)
}

fun findMissingSeatId(seats: List<Seat>): Int {
    val sortedSeatIds = seats.map { it.seatId }.sortedBy { it }

    return recursiveSearchMissingElementInConsecutiveList(sortedSeatIds)
}

// Recursive binary search knowing that each element at position i will have value i + offset
// With offset the first value of the list
fun recursiveSearchMissingElementInConsecutiveList(list: List<Int>, offset: Int = list.first(), lower: Int = 0, upper: Int = list.size - 1): Int {
    val mid = (upper + lower) / 2
    if (list[mid] != mid + offset && list[mid-1] == mid - 1 + offset) {
        return mid + offset
    }
    if (list[mid] != mid + offset && list[mid-1] != mid - 1 + offset) {
        return recursiveSearchMissingElementInConsecutiveList(list, offset, lower, mid)
    }
    if (list[mid] == mid + offset) {
        return recursiveSearchMissingElementInConsecutiveList(list, offset, mid, upper)
    }
    return - 1
}

fun parseSeat(seatCode: String): Seat {
    return Seat.getSeatFromCode(seatCode)
}

class Seat(row: Int, column: Int) {
    val seatId = row * 8 + column

    companion object {
        fun getSeatFromCode(code: String): Seat {
            val rowCode = code.take(7)
            val columnCode = code.takeLast(3)

            val row = calculateSeatRowOrColumnFromCode(rowCode)
            val column = calculateSeatRowOrColumnFromCode(columnCode)

            return Seat(row, column)
        }

        private fun calculateSeatRowOrColumnFromCode(code: String): Int {
            var lower = 0
            var upper = 2F.pow(code.length).toInt()
            var mid: Int

            for (idx in code) {
                mid = (lower + upper) / 2
                if (idx == 'F' || idx == 'L') {
                    upper = mid
                }
                if (idx == 'B' || idx == 'R') {
                    lower = mid
                }
            }
            return lower
        }
    }
}

//// Binary search knowing that each element at position i will have value i + offset
//// With offset the first value of the list
//fun searchMissingElementInConsecutiveList(list: List<Int>): Int {
//    var lower = 0
//    var upper = list.size - 1
//    var mid: Int
//    val offset = list.first()
//
//    while (upper >= lower) {
//        mid = (upper + lower) / 2
//        if (list[mid] != mid + offset && list[mid-1] == mid - 1 + offset) {
//            return mid + offset
//        }
//        if (list[mid] != mid + offset && list[mid-1] != mid - 1 + offset) {
//            upper = mid
//        }
//        if (list[mid] == mid + offset) {
//            lower = mid
//        }
//    }
//    return - 1
//}