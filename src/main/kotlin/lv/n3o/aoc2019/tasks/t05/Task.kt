package lv.n3o.aoc2019.tasks.t05

import lv.n3o.aoc2019.tasks.IntComp
import lv.n3o.aoc2019.tasks.Memory
import lv.n3o.aoc2019.tasks.Task

@Suppress("unused")
class Task : Task() {
//    private val programNumbers by lazy { "3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99".split(",").map(String::toInt) }
    private val programNumbers by lazy { data05.split(",").map(String::toInt) }

    override fun a(): String {
        val memory = Memory(programNumbers.toMutableList())
        val outputs = mutableListOf<Int>()
        IntComp(memory, { 1 }, {
            println(it)
            outputs.add(it)
        }).runToHalt()
        return "${outputs.last()}"
    }

    override fun b() : String {
        val memory = Memory(programNumbers.toMutableList())
        val outputs = mutableListOf<Int>()
        IntComp(memory, { 5 }, {
            println(it)
            outputs.add(it)
        }).runToHalt()
        return "${outputs.last()}"
    }
}