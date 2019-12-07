package lv.n3o.aoc2019.tasks.t05

import lv.n3o.aoc2019.tasks.IntComp
import lv.n3o.aoc2019.tasks.Memory
import lv.n3o.aoc2019.tasks.Task
import lv.n3o.aoc2019.tasks.doComputation

@Suppress("unused")
class Task : Task() {
    private val programNumbers by lazy { data05.split(",").map(String::toInt) }

    override fun a(): String {
        val memory = Memory(programNumbers.toMutableList())
        return "${doComputation(memory, listOf(1)).last()}"
    }

    override fun b() : String {
        val memory = Memory(programNumbers.toMutableList())
        return "${doComputation(memory, listOf(5)).last()}"
    }
}