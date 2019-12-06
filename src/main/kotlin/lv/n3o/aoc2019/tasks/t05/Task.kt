package lv.n3o.aoc2019.tasks.t05

import lv.n3o.aoc2019.tasks.IntComp
import lv.n3o.aoc2019.tasks.Memory
import lv.n3o.aoc2019.tasks.Task

@Suppress("unused")
class Task : Task() {
    private val programNumbers by lazy { data05.split(",").map(String::toInt) }

    override fun a(): String {
        val memory = Memory(programNumbers.toMutableList())
        val outputs = mutableListOf<Int>()
        IntComp(memory, { 1 }, {
            outputs.add(it)
        }).runToHalt()
        return "${outputs.last()}"
    }

    override fun b() : String {
        val memory = Memory(programNumbers.toMutableList())
        val outputs = mutableListOf<Int>()
        IntComp(memory, { 5 }, {
            outputs.add(it)
        }).runToHalt()
        return "${outputs.last()}"
    }
}