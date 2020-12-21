package year2019.day05

class Pointer {
    private var pointerValue: Int = 0

    val value: Int
        get() = this.pointerValue

    operator fun plus(other: Int): Pointer {
        pointerValue += other
        return this
    }

    operator fun compareTo(size: Int): Int {
        return compareValues(this.pointerValue, size)
    }

    fun update(newValue: Int) {
        pointerValue = newValue
    }
}
