package lv.n3o.aoc2019.tasks.t09

import lv.n3o.aoc2019.tasks.Memory
import lv.n3o.aoc2019.tasks.Task
import lv.n3o.aoc2019.tasks.doComputation

@Suppress("unused")
class Task : Task() {
    private val programNumbers by lazy { data09.split(",").map(String::toLong) }

    override fun a(): String {
        return doComputation(Memory(programNumbers.toMutableList()), 1L).first().toString()
    }

    override fun b(): String {
        return doComputation(Memory(programNumbers.toMutableList()), 2L).first().toString()
    }
}