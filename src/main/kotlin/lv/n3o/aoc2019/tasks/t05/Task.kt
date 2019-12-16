package lv.n3o.aoc2019.tasks.t05

import lv.n3o.aoc2019.tasks.Task
import lv.n3o.aoc2019.tasks.doComputation

@Suppress("unused")
class Task : Task() {
    private val programNumbers by lazy { input.split(",").map(String::toLong) }

    override fun a(): String {
        return "${doComputation(programNumbers, 1L).last()}"
    }

    override fun b(): String {
        return "${doComputation(programNumbers, 5L).last()}"
    }

}