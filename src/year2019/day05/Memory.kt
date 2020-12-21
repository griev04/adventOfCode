package year2019.day05

class Memory {
    private val memory = mutableListOf<Int>()

    fun initialize(instructions: List<Int>): Memory {
        reset()
        memory.addAll(instructions)
        return this
    }

    fun access(address: Int): Int {
        return memory[address]
    }

    fun update(address: Int, value: Int): Int {
        memory[address] = value
        return value
    }

    fun reset() {
        memory.clear()
    }

    fun getSize(): Int {
        return memory.size
    }
}
