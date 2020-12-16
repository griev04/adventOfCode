package year2020.day16

import common.TextFileParser

fun main() {
    val ticketValidator = createTicketValidator("src/year2020.day16/input.txt")
    println("Part 1")
    val errorRate = ticketValidator.getErrorRate()
    println(errorRate)

    println("Part 2")
    val departureIndex = ticketValidator.getDepartureIndex()
    println(departureIndex)

    println("Your ticket was:\n${ticketValidator.getMyTicket()}")
}

class TicketValidator(private val rules: List<Rule>, private val myTicket: Ticket, private val otherTickets: List<Ticket>) {
    private lateinit var fieldsMapping: Map<String, Int>
    private lateinit var validTickets: List<Ticket>
    private var errorRate: Int = 0

    init {
        resolveTicketsData()
    }

    fun getErrorRate(): Int {
        return errorRate
    }

    fun getDepartureIndex(): Long {
        val fieldPositions = fieldsMapping.filterKeys { it.startsWith("departure") }.values
        return fieldPositions.fold(1L) { acc, position ->
            acc * myTicket.values[position]
        }
    }

    fun getMyTicket(): Map<String, Int> {
        return fieldsMapping.mapValues { myTicket.values[it.value] }
    }

    private fun resolveTicketsData() {
        validateTickets(otherTickets)
        matchFieldNamesToValues()
    }

    private fun validateTickets(tickets: List<Ticket>) {
        val inspectedTickets = tickets.map { it.inspectValidity(rules) }
        validTickets = inspectedTickets.filter { it.isValid }
        errorRate = inspectedTickets.fold(0) { acc, ticket -> acc + ticket.invalidFields.sum() }
    }

    private fun matchFieldNamesToValues() {
        val validRulesPerField = getValidRulesPerField()
        fieldsMapping = getFieldsMapping(validRulesPerField)
    }

    private fun getFieldsMapping(validRulesPerField: Map<Int, MutableSet<Rule>>): Map<String, Int> {
        val fieldIdToRuleMap = mutableMapOf<Int, Rule>()
        val sortedRules = validRulesPerField.entries.sortedBy { it.value.size }
        sortedRules.forEach { mapEntry ->
            val (fieldId, validRules) = mapEntry
            val validRule = validRules.filter {
                !fieldIdToRuleMap.values.contains(it)
            }
            fieldIdToRuleMap[fieldId] = validRule.first()
        }
        return fieldIdToRuleMap.entries.associate{ (key,value)-> value.name to key}
    }

    private fun getValidRulesPerField(): Map<Int, MutableSet<Rule>> {
        val validRulesPerField = rules.indices.map { it to rules.toMutableSet() }.toMap()

        val allTicketValues = validTickets.map { it.values }.flatten()

        allTicketValues.forEachIndexed { listId, fieldValue ->
            val fieldId = listId % rules.size
            rules.forEach { rule ->
                if (validRulesPerField[fieldId]?.contains(rule) == true && !rule.isValid(fieldValue)) {
                    validRulesPerField[fieldId]?.remove(rule)
                }
            }
        }
        return validRulesPerField
    }
}

class Rule(val name: String, private val validityRanges: List<IntRange>) {
    fun isValid(value: Int): Boolean {
        return validityRanges.map { range -> value in range }.any { it }
    }
}

class Ticket(val values: List<Int>) {
    var isValid = true
    val invalidFields = mutableListOf<Int>()

    fun inspectValidity(rules: List<Rule>): Ticket {
        isValid = checkTicketValidity(rules)
        return this
    }

    private fun checkTicketValidity(rules: List<Rule>): Boolean {
        val fieldsValidity = values.map { value -> checkValue(value, rules) }
        return fieldsValidity.none { !it }
    }

    private fun checkValue(value: Int, rules: List<Rule>): Boolean {
        val isValidField = rules.map { field -> field.isValid(value) }.any { it }
        if (!isValidField) invalidFields.add(value)
        return isValidField
    }
}

fun createTicketValidator(fileName: String): TicketValidator {
    return TextFileParser.parseFile(fileName) { text ->
        val (fieldsEntries, myTicketEntry, otherTicketsEntries) =
                text.split("\n\n").map { it.split("\n") }

        val fields = parseFields(fieldsEntries)

        val myTicket = Ticket(myTicketEntry[1].split(",").map { it.toInt() })

        val otherTickets = otherTicketsEntries.subList(1, otherTicketsEntries.size)
                .filter { it.isNotEmpty() }
                .map { line ->
                    Ticket(line.split(",").map { it.toInt() })
                }

        TicketValidator(fields, myTicket, otherTickets)
    }
}

private fun parseFields(fieldLines: List<String>): List<Rule> {
    return fieldLines.map { line ->
        val (fieldName, values) = line.split(": ")
        val rangeData = values.split(" or ")
        val ranges = rangeData.map { range ->
            val (min, max) = range.split("-").map { it.toInt() }
            IntRange(min, max)
        }
        Rule(fieldName, ranges)
    }
}
