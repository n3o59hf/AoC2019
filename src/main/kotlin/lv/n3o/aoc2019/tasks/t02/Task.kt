package lv.n3o.aoc2019.tasks.t02

import lv.n3o.aoc2019.tasks.Task

@Suppress("unused")
class Task : Task() {
    private val programNumbers by lazy { data02.split(",").map(String::toInt) }

    class Memory(val backing: MutableList<Int>) {
        fun readDirect(position: Int) = backing[position]
        fun readIndirect(position: Int) = backing[backing[position]]

        fun writeDirect(position: Int, value: Int) {
            backing[position] = value
        }

        fun writeIndirect(position: Int, value: Int) {
            backing[backing[position]] = value
        }

        override fun toString() = "Memory($backing)"
    }

    class IntComp(val memory: Memory) {
        var pc = 0

        fun step(): Boolean {
            val code = memory.readDirect(pc)
            val op = OpCode.getOpcode(code)
            if (op == OpCode.HALT) return false
            if (op == OpCode.ERR) error("Unknown opcode $code at position $pc")
            op.interpret(memory, pc)
            pc += op.size
            return true
        }

        fun runToHalt() {
            while (step()) { /* Intentionally left blank */
            }
        }
    }

    enum class OpCode(val code: Int, paramCount: Int) {
        ERR(0, 0),
        ADD(1, 3),
        MUL(2, 3),
        HALT(99, 0);

        val size = paramCount + 1

        fun interpret(memory: Memory, pc: Int) {
            when (this) {
                ERR -> {
                    error("Non existent opcode")
                }
                ADD -> memory.writeIndirect(pc + 3, memory.readIndirect(pc + 1) + memory.readIndirect(pc + 2))
                MUL -> memory.writeIndirect(pc + 3, memory.readIndirect(pc + 1) * memory.readIndirect(pc + 2))
                HALT -> error("Should not be executed")
            }
        }

        companion object {
            val opcodeCache = Array(255) { ERR }.apply {
                values().forEach {
                    this[it.code] = it
                }
            }

            fun getOpcode(code: Int) = try {
                opcodeCache[code]
            } catch (e: ArrayIndexOutOfBoundsException) {
                ERR
            }
        }
    }

    override fun a(): String {
        val memory = Memory(programNumbers.toMutableList())
        memory.writeDirect(1, data02InputA)
        memory.writeDirect(2, data02InputB)
        IntComp(memory).runToHalt()
        return memory.readDirect(0).toString()
    }

    override fun b(): String {
        return (0..99).flatMap { a -> (0..99).map { b -> a to b } }.asSequence().first { (a, b) ->
            val memory = Memory(programNumbers.toMutableList())
            memory.writeDirect(1, a)
            memory.writeDirect(2, b)
            IntComp(memory).runToHalt()
            memory.readDirect(0) == data02FinalResult
        }.let { (a, b) -> "${100 * a + b}" }
    }
}
