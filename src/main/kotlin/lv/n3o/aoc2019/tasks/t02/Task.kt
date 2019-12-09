package lv.n3o.aoc2019.tasks.t02

import lv.n3o.aoc2019.tasks.Memory
import lv.n3o.aoc2019.tasks.Mode
import lv.n3o.aoc2019.tasks.Task
import lv.n3o.aoc2019.tasks.doComputation

@Suppress("unused")
class Task : Task() {
    private val programNumbers by lazy { input.split(",").map(String::toLong) }

    override fun a(): String {
        val memory = Memory(programNumbers.toMutableList())
        memory[Mode.IMMEDIATE, 1] = 12L
        memory[Mode.IMMEDIATE, 2] = 2L
        doComputation(memory)
        return memory[Mode.IMMEDIATE, 0].toString()
    }

    override fun b(): String {
        return (0L..99L).flatMap { a -> (0L..99L).map { b -> a to b } }.asSequence().first { (a, b) ->
            val memory = Memory(programNumbers.toMutableList())
            memory[Mode.IMMEDIATE, 1] = a
            memory[Mode.IMMEDIATE, 2] = b
            doComputation(memory)
            memory[Mode.IMMEDIATE, 0] == 19690720L
        }.let { (a, b) -> "${100 * a + b}" }
    }
}
