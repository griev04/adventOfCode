package year2020.day06

import common.TextFileParser

fun main() {
    val groups = TextFileParser.parseGroupedData("src/year2020/day06/input.txt") { parseGroup(it) }

    println("Day 06 Part 1")
    val totalUniqueGroupAnswers = getCountOfUniqueAnswersPerGroup(groups)
    println(totalUniqueGroupAnswers)

    println("Day 06 Part 2")
    val totalCommonGroupAnswers = getCountOfCommonAnswersPerGroup(groups)
    println(totalCommonGroupAnswers)

}

private fun parseGroup(text: List<String>): Group {
    return Group(text.map { answersByPerson ->
        Person(answersByPerson.split("").filter { inner -> inner.isNotEmpty() })
    })
}

// Another cool approach would be to use, together with sets, groupBy to count occurrences of each letter
private fun getCountOfUniqueAnswersPerGroup(groups: List<Group>): Int {
    val uniqueAnswersPerGroup = groups.map { group ->
        val allAnswers = group.people.map { person ->
            person.answers
        }
        // spread all answers and get set to eliminate duplicates
        val uniqueAnswers = allAnswers.flatten().toSet()
        uniqueAnswers
    }
    return uniqueAnswersPerGroup.sumBy { it.size }
}

private fun getCountOfCommonAnswersPerGroup(groups: List<Group>): Int {
    val commonAnswersPerGroup = groups.map { group ->
        val setsOfAnswers = group.people.map { person ->
            person.answers.toSet()
        }
        // intersect all answer sets to get common values
        val commonAnswers = setsOfAnswers.reduce { acc, set ->
            acc.intersect(set)
        }
        commonAnswers
    }
    return commonAnswersPerGroup.sumBy { it.size }
}

class Group(val people: List<Person>)

class Person(val answers: List<String>)