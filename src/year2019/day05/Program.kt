package year2019.day05

class Program(instructions: List<Int>) {
    private val sourceInstructions = instructions
    private val memory = Memory().initialize(instructions)

    fun initializeProgram(noun: Int, verb: Int): Program {
        memory.initialize(sourceInstructions)
        memory.update(1, noun)
        memory.update(2, verb)
        return this
    }

    fun getValue(position: Int): Int {
        return memory.access(position)
    }

    fun run(): Program {
        val pointer = Pointer()
        while (pointer < memory.getSize()) {
            val instructionCode = memory.access(pointer.value)
            if (instructionCode == 99) break
            val instruction = Instruction(instructionCode)
            instruction.execute(memory, pointer)
        }
        return this
    }
}
