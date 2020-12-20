package year2020.day20

class Tile(val number: Int, var values: List<String>) {
    lateinit var trimmedValues: List<String>
    var matches = mapOf<Tile, String>()
    var isCorner = false
    var isSolved = false
    val map = mutableMapOf<Pair<Int, Int>, Tile>()

    override fun toString(): String {
        return values.joinToString("\n")
    }

    fun trimBorders() {
        var newValues = values.toMutableList()
        newValues.removeAt(newValues.size - 1)
        newValues.removeAt(0)
        newValues = newValues.map { row -> row.subSequence(1, row.length - 1).toString() }.toMutableList()
        trimmedValues = newValues
    }

    fun setAsTopLeftCorner() {
        if (isCorner) {
            while (!isTopLeft()) {
                rotate()
            }
            isSolved = true
        }
    }

    private fun isTopLeft(): Boolean {
        val values = getBorders().filterKeys { it == Location.BOTTOM || it == Location.RIGHT }.values
        val possibleValues = matches.values.map { listOf(it, it.reversed()) }.flatten()
        return values.filter { possibleValues.contains(it) }.size == 2
    }

    fun arrangeToMatchingPosition(target: String, targetLocation: Location) {
        if (isSolved) {
            return
        }
        val currentLocation = findBorderLocation(target)
        val rotations = (locations.size + locations.indexOf(targetLocation) - locations.indexOf(currentLocation)) % locations.size
        rotate(rotations)

        val targetBorder = getBorders()[targetLocation]
        if (targetBorder == target.reversed() && (targetLocation == Location.LEFT || targetLocation == Location.RIGHT)) {
            flipVertically()
        }
        if (targetBorder == target.reversed() && (targetLocation == Location.TOP || targetLocation == Location.BOTTOM)) {
            flipHorizontally()
        }
        isSolved = true
    }

    fun findBorderLocation(target: String): Location {
        val bordersLocation = getBorders()
        return bordersLocation.filterValues { it.contains(target) || it.reversed().contains(target) }.keys.first()
    }

    fun getBorderAtLocation(location: Location): String {
        return getBorders()[location]!!
    }

    fun rotate(times: Int = 1) {
        repeat(times) {
            val rows = mutableListOf<String>()
            for (col in values[0].indices) {
                var newRow = ""
                for (row in values.indices) {
                    newRow += values[row][col]
                }
                rows.add(newRow.reversed())
            }
            values = rows
        }
    }

    fun flipHorizontally() {
        values = values.map { row -> row.reversed() }
    }

    fun flipVertically() {
        values = values.reversed()
    }

    fun addMatches(matches: Map<Tile, String>) {
        this.matches = matches
        if (matches.size == 2) {
            isCorner = true
        }
    }

    fun getBorders(): Map<Location, String> {
        var leftBorder = ""
        var rightBorder = ""
        values.forEach { line ->
            leftBorder += line.first()
            rightBorder += line.last()
        }
        return mapOf(
                Location.TOP to values.first(),
                Location.BOTTOM to values.last(),
                Location.LEFT to leftBorder.reversed(),
                Location.RIGHT to rightBorder.reversed()
        )
    }

    companion object {
        private val locations = listOf(Location.TOP, Location.RIGHT, Location.BOTTOM, Location.LEFT)

        fun getOppositeDirection(direction: Location): Location {
            val index = locations.indexOf(direction)
            return locations[(index + 2) % locations.size]
        }
    }
}