package lv.n3o.aoc2019.tasks.t02

import lv.n3o.aoc2019.tasks.IntComp
import lv.n3o.aoc2019.tasks.Memory
import lv.n3o.aoc2019.tasks.Task
import lv.n3o.aoc2019.tasks.doComputation

@Suppress("unused")
class Task : Task() {
    private val programNumbers by lazy { data02.split(",").map(String::toInt) }

    override fun a(): String {
        val memory = Memory(programNumbers.toMutableList())
        memory.writeDirect(1, data02InputA)
        memory.writeDirect(2, data02InputB)
        doComputation(memory)
        return memory.readDirect(0).toString()
    }

    override fun b(): String {
        return (0..99).flatMap { a -> (0..99).map { b -> a to b } }.asSequence().first { (a, b) ->
            val memory = Memory(programNumbers.toMutableList())
            memory.writeDirect(1, a)
            memory.writeDirect(2, b)
            doComputation(memory)
            memory.readDirect(0) == data02FinalResult
        }.let { (a, b) -> "${100 * a + b}" }
    }
}
