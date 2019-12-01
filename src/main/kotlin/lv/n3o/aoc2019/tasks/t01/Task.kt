package lv.n3o.aoc2019.tasks.t01

import lv.n3o.aoc2019.tasks.Task
import lv.n3o.aoc2019.tasks.cleanLines

@Suppress("unused")
class Task : Task() {
    val data = data01.cleanLines.map(String::toInt)

    override fun a(): String =
        data.map { it / 3 - 2 }.sum().toString()


    override fun b(): String = data
        .map { it / 3 - 2 }
        .flatMap {
            var fuel = it
            sequence {
                do {
                    if (fuel > 0)
                        yield(fuel)
                    fuel = fuel / 3 - 2
                } while (fuel > 0)
            }.toList()
        }
        .sum()
        .toString()
}