package lv.n3o.aoc2019.tasks.t19

import lv.n3o.aoc2019.coords.C2
import lv.n3o.aoc2019.coords.Coord2d
import lv.n3o.aoc2019.tasks.*
import lv.n3o.aoc2019.tasks.Task

@Suppress("unused")
class Task : Task() {
    private val programNumbers: List<Long> by lazy { input.split(",").map(String::toLong) }

    val coordinatesToCheck: List<Coord2d> by lazy {
        (0 until 50)
            .flatMap { y -> (0 until 50).map { x -> C2(x, y) } }
    }
    val map by lazy {
        mutableMapOf<Coord2d, Boolean>().withLazy { c ->
            doComputation(
                programNumbers,
                c.x.toLong(),
                c.y.toLong()
            ).first() == 1L
        }
    }

    override fun a() = coordinatesToCheck.map { map[it] }.count { it }.toString()

    override fun b(): String {
        val down = C2(0, 1)
        val right = C2(1, 0)

        val searchSize = 100
        val ssC = C2(searchSize - 1, -searchSize + 1)

        var bl: Coord2d = map.backingMap
            .filter { it.value && it.key.x > searchSize && it.key.y > searchSize }
            .keys
            .sortedBy { it.y }.minBy { it.x } ?: C2(0, searchSize)

        while (!(map[bl] && map[bl + ssC])) {
            bl += if (map[bl]) down else right
        }

        return (bl.x * 10000 + (bl + ssC).y).toString()
    }
}