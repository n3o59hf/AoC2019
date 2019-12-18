package lv.n3o.aoc2019.tasks

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

typealias InputReceiver = suspend () -> Long

class Memory(initial: List<Long>) {
    private var backing = initial.toLongArray()
    var relativeOffset = 0

    operator fun get(mode: Mode, position: Int) = read(mode, position)
    operator fun set(mode: Mode, position: Int, value: Long) = write(mode, position, value)

    private operator fun get(position: Int): Long {
        ensureCapacity(position + 1)
        return backing[position]
    }

    private operator fun set(position: Int, value: Long) {
        ensureCapacity(position + 1)
        backing[position] = value
    }

    private fun readDirect(position: Int) = this[position]
    private fun readIndirect(position: Int) = this[this[position].toInt()]
    private fun readRelative(position: Int) = this[this[position].toInt() + relativeOffset]

    private fun writeDirect(position: Int, value: Long) {
        this[position] = value
    }

    private fun writeIndirect(position: Int, value: Long) {
        this[this[position].toInt()] = value
    }

    private fun writeRelative(position: Int, value: Long) {
        this[this[position].toInt() + relativeOffset] = value
    }

    private fun read(mode: Mode, position: Int) = when (mode) {
        Mode.POSITION -> readIndirect(position)
        Mode.IMMEDIATE -> readDirect(position)
        Mode.RELATIVE -> readRelative(position)
    }

    private fun write(mode: Mode, position: Int, value: Long) = when (mode) {
        Mode.POSITION -> writeIndirect(position, value)
        Mode.IMMEDIATE -> writeDirect(position, value)
        Mode.RELATIVE -> writeRelative(position, value)
    }

    private fun ensureCapacity(size: Int) {
        if (backing.size < size) {
            val newArray = LongArray(size)
            backing.copyInto(newArray)
            backing = newArray
        }
    }

    override fun toString() = "Memory($backing)"
}

fun IntComp(
    memory: Memory,
    input: ReceiveChannel<Long>,
    output: SendChannel<Long>
) = IntComp(memory, output) {
    input.receive()
}

class IntComp(
    val memory: Memory,
    val output: SendChannel<Long>,
    val inputReceiver: InputReceiver
) {
    var pc = 0

    suspend fun step(): Boolean {
        val code = memory[Mode.IMMEDIATE, pc].toInt()
        val op = OpCode.getOpcode(code)
        val modes = Mode.getModes(code, op)
        if (op == OpCode.HALT) {
            output.close()
            return false
        }
        if (op == OpCode.ERR) error("Unknown opcode $code at position $pc")
        try {
            op.interpret(this, modes)
        } catch (e: ClosedReceiveChannelException) {
            return false
        }
        pc += op.size
        return true
    }

    suspend fun runToHalt() {
        while (step()) { /* Intentionally left blank */
        }
    }

    override fun toString(): String {
        return "IntComp(pc=$pc)"
    }
}

fun doComputation(
    memory: List<Long>,
    vararg input: Long
) = doComputation(Memory(memory), *input)

fun doComputation(
    memory: Memory,
    vararg input: Long
): List<Long> = runBlocking {
    val inputs = Channel<Long>(Channel.UNLIMITED)
    input.forEach { inputs.send(it) }

    val outputs = Channel<Long>(Channel.UNLIMITED)
    IntComp(
        memory,
        input = inputs,
        output = outputs
    ).runToHalt()

    outputs.toList()
}

class StepComputer(memory: Memory) {
    private val inputChannel = Channel<Long>(capacity = 1)
    private val outputChannel = Channel<Long>(capacity = 1)

    private val comp = IntComp(memory, inputChannel, outputChannel)

    suspend fun start() {
        comp.runToHalt()
    }

    suspend fun doStep(input: Long): Long {
        inputChannel.send(input)
        return outputChannel.receive()
    }

    fun finish() {
        inputChannel.close()
    }
}

suspend fun doStepComputation(
    coroutineScope: CoroutineScope,
    memory: List<Long>,
    scope: suspend StepComputer.() -> Unit
): Unit =
    doStepComputation(coroutineScope, Memory(memory), scope)


suspend fun doStepComputation(
    coroutineScope: CoroutineScope,
    memory: Memory,
    scope: suspend StepComputer.() -> Unit
) {
    StepComputer(memory).apply {
        coroutineScope.launch { start() }
        scope()
        finish()
    }
}

enum class OpCode(val code: Int, val paramCount: Int) {
    ERR(0, 0),
    ADD(1, 3),
    MUL(2, 3),
    INPUT(3, 1),
    OUTPUT(4, 1),
    JIT(5, 2),
    JIF(6, 2),
    LT(7, 3),
    EQ(8, 3),
    OFFSET(9, 1),
    HALT(99, 0);

    val size = paramCount + 1

    suspend fun interpret(comp: IntComp, modes: Array<Mode>) {
        when (this) {
            ERR -> error("Non existent opcode")
            ADD -> comp.memory[modes[2], comp.pc + 3] =
                comp.memory[modes[0], comp.pc + 1] + comp.memory[modes[1], comp.pc + 2]
            MUL -> comp.memory[modes[2], comp.pc + 3] =
                comp.memory[modes[0], comp.pc + 1] * comp.memory[modes[1], comp.pc + 2]
            INPUT -> {
                comp.memory[modes[0], comp.pc + 1] = comp.inputReceiver()
            }
            JIT -> if (comp.memory[modes[0], comp.pc + 1] != 0L) {
                comp.pc = comp.memory[modes[1], comp.pc + 2].toInt() - size
            }
            JIF -> if (comp.memory[modes[0], comp.pc + 1] == 0L) {
                comp.pc = comp.memory[modes[1], comp.pc + 2].toInt() - size
            }
            LT -> {
                comp.memory[modes[2], comp.pc + 3] =
                    if (comp.memory[modes[0], comp.pc + 1] < comp.memory[modes[1], comp.pc + 2]) 1 else 0
            }
            EQ -> {
                comp.memory[modes[2], comp.pc + 3] =
                    if (comp.memory[modes[0], comp.pc + 1] == comp.memory[modes[1], comp.pc + 2]) 1 else 0
            }
            OUTPUT -> comp.output.send(
                comp.memory[modes[0], comp.pc + 1]
            )
            OFFSET -> comp.memory.relativeOffset += comp.memory[modes[0], comp.pc + 1].toInt()
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
    IMMEDIATE(1),
    RELATIVE(2);

    companion object {
        private val modeCache = Array((values().map { it.modeConstant }.max() ?: 0) + 1) { POSITION }.apply {
            values().forEach {
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
