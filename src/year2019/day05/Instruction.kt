package year2019.day05

class Instruction(instructionCode: Int) {
    private var opCode: Int
    private var modes = emptyList<Int>()
    private var parameters = mutableMapOf<Int, Int>()
    private var currentPointer: Int = 0
    lateinit var cache: Memory

    init {
        if (instructionCode < 100) {
            opCode = instructionCode
        } else {
            val codeString = instructionCode.toString()
            val rest = codeString.take(codeString.length - 2)
            opCode = codeString.takeLast(2).toInt()
            modes = rest.map { it.toString().toInt() }.reversed()
        }
    }

    fun execute(memory: Memory, pointer: Pointer): Boolean {
        setExecutionContext(memory, pointer)
        when (opCode) {
            1 -> update(2, access(0) + access(1))
            2 -> update(2, access(0) * access(1))
            3 -> update(0, input())
            4 -> output(access(0))
            5 -> return if (access(0) != 0) {
                pointer.update(access(1))
                true
            } else {
                pointer + 3
                true
            }
            6 -> return if (access(0) == 0) {
                pointer.update(access(1))
                true
            } else {
                pointer + 3
                true
            }
            7 -> update(2, if ((access(0) < access(1))) 1 else 0)
            8 -> update(2, if ((access(0) == access(1))) 1 else 0)
        }

        pointer + parameters.size + 1
        return true
    }

    private fun update(parameter: Int, value: Int): Int {
        return cache.update(computeParameter(parameter), value)
    }

    private fun access(parameter: Int): Int {
        return cache.access(computeParameter(parameter))
    }

    private fun computeParameter(param: Int): Int {
        parameters[param].let { if (it != null) return it }
        val value = if ((param) >= modes.size || modes[param] == 0) {
            cache.access(currentPointer + param + 1)
        } else {
            currentPointer + param + 1
        }
        parameters[param] = value
        return value
    }

    private fun setExecutionContext(memory: Memory, pointer: Pointer) {
        cache = memory
        currentPointer = pointer.value
    }

    private fun input(): Int {
        print("Write any number: ")
        return Integer.valueOf(readLine())
    }

    private fun output(value: Int) {
        println(value)
    }
}
