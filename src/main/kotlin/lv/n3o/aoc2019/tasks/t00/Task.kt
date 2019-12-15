package lv.n3o.aoc2019.tasks.t00

import lv.n3o.aoc2019.tasks.Task
import lv.n3o.aoc2019.tasks.cleanLines

@Suppress("unused")
class Task : Task() {
    val data by lazy { input.cleanLines }

    override fun a(): String {
        log("returning data from $data in position 0")
        return data[0]
    }

    override fun b(): String {
        log("returning data from $data in position 1")
        return data[1]
    }
}