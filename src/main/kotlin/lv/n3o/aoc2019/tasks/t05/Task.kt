package lv.n3o.aoc2019.tasks.t05

import lv.n3o.aoc2019.tasks.Memory
import lv.n3o.aoc2019.tasks.Task
import lv.n3o.aoc2019.tasks.doComputation

@Suppress("unused")
class Task : Task() {
    private val programNumbers by lazy { input.split(",").map(String::toLong) }

    override fun a(): String {
        val memory = Memory(programNumbers.toMutableList())
        return "${doComputation(memory, 1L).last()}"
    }

    override fun b(): String {
        val memory = Memory(programNumbers.toMutableList())
        return "${doComputation(memory, 5L).last()}"
    }

}