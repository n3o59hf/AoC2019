package lv.n3o.aoc2019.tasks

class Memory(val backing: MutableList<Int>) {
    fun readDirect(position: Int) = backing[position]
    fun readIndirect(position: Int) = backing[backing[position]]

    fun writeDirect(position: Int, value: Int) {
        backing[position] = value
    }

    fun writeIndirect(position: Int, value: Int) {
        backing[backing[position]] = value
    }

    fun read(mode: Mode, position: Int) = when (mode) {
        Mode.POSITION -> readIndirect(position)
        Mode.IMMEDIATE -> readDirect(position)
    }

    fun write(mode: Mode, position: Int, value: Int) = when (mode) {
        Mode.POSITION -> writeIndirect(position, value)
        Mode.IMMEDIATE -> writeDirect(position, value)
    }

    override fun toString() = "Memory($backing)"
}

class IntComp(val memory: Memory, val input: () -> Int = { 0 }, val output: (Int) -> Unit = {}) {
    var pc = 0

    fun step(): Boolean {
        val code = memory.readDirect(pc)
        val op = OpCode.getOpcode(code)
        val modes = Mode.getModes(code, op)
        if (op == OpCode.HALT) return false
        if (op == OpCode.ERR) error("Unknown opcode $code at position $pc")
        op.interpret(this, modes)
        pc += op.size
        return true
    }

    fun runToHalt() {
        while (step()) { /* Intentionally left blank */
        }
    }

    override fun toString(): String {
        return "IntComp(pc=$pc)"
    }
}

enum class OpCode(val code: Int, val paramCount: Int) {
    ERR(0, 0),
    ADD(1, 3),
    MUL(2, 3),
    INPUT(3, 1),
    OUTPUT(4, 1),
    JIT(5,2),
    JIF(6,2),
    LT(7,3),
    EQ(8,3),
    HALT(99, 0);

    val size = paramCount + 1

    fun interpret(comp: IntComp, modes: Array<Mode>) {
        when (this) {
            ERR -> {
                error("Non existent opcode")
            }
            ADD -> comp.memory.write(
                modes[2], comp.pc + 3,
                comp.memory.read(modes[0], comp.pc + 1) + comp.memory.read(modes[1], comp.pc + 2)
            )
            MUL -> comp.memory.write(
                modes[2], comp.pc + 3,
                comp.memory.read(modes[0], comp.pc + 1) * comp.memory.read(modes[1], comp.pc + 2)
            )
            INPUT -> comp.memory.write(
                modes[0],
                comp.pc + 1,
                comp.input()
            )
            JIT -> if (comp.memory.read(modes[0],comp.pc+1) != 0) {
                comp.pc = comp.memory.read(modes[1], comp.pc + 2) - size
            }
            JIF -> if (comp.memory.read(modes[0],comp.pc+1) == 0) {
                comp.pc = comp.memory.read(modes[1], comp.pc + 2) - size
            }
            LT -> {
                comp.memory.write(
                    modes[2],
                    comp.pc + 3,
                    if (comp.memory.read(modes[0], comp.pc + 1) < comp.memory.read(modes[1], comp.pc + 2)) 1 else 0
                )
            }
            EQ -> {
                comp.memory.write(
                    modes[2],
                    comp.pc + 3,
                    if (comp.memory.read(modes[0], comp.pc + 1) == comp.memory.read(modes[1], comp.pc + 2)) 1 else 0
                )
            }
            OUTPUT -> comp.output(
                comp.memory.read(modes[0], comp.pc + 1)
            )
            HALT -> error("Should not be executed")
        }
    }

    companion object {
        private val opcodeCache = Array(100) { ERR }.apply {
            values().forEach {
                this[it.code] = it
            }
        }

        fun getOpcode(code: Int) = try {
            opcodeCache[code % 100]
        } catch (e: ArrayIndexOutOfBoundsException) {
            ERR
        }
    }
}

enum class Mode(val modeConstant: Int) {
    POSITION(0),
    IMMEDIATE(1);

    companion object {
        private val modeCache = Array(10) { Mode.POSITION }.apply {
            Mode.values().forEach {
                this[it.modeConstant] = it
            }
        }

        fun getModes(codeIn: Int, opCode: OpCode): Array<Mode> {
            var code = codeIn / 10
            return Array(opCode.paramCount) {
                code /= 10
                modeCache[code % 10]
            }
        }
    }
}
