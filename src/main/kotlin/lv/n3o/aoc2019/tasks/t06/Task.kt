package lv.n3o.aoc2019.tasks.t06

import lv.n3o.aoc2019.tasks.Task
import lv.n3o.aoc2019.tasks.cleanLines

@Suppress("unused")
class Task : Task() {
    val data by lazy {
        input.cleanLines.map { l -> l.split(")") }.associateBy({ it.last() }, { it.first() })
    }

    fun getRoute(start: String) = generateSequence(start) {
        data[it]
    }.drop(1).toList()

    override fun a() = "${data.keys.sumBy { getRoute(it).size }}"

    override fun b(): String {
        val youRoute = getRoute("YOU").toSet()
        val sanRoute = getRoute("SAN").toSet()

        val youUnique = youRoute - sanRoute
        val sanUnique = sanRoute - youRoute
        return "${(youUnique + sanUnique).size}"
    }

}