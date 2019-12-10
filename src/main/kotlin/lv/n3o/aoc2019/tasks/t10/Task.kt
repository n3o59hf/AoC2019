package lv.n3o.aoc2019.tasks.t10

import lv.n3o.aoc2019.coords.C2
import lv.n3o.aoc2019.coords.clockAngle
import lv.n3o.aoc2019.coords.linePoints
import lv.n3o.aoc2019.tasks.Task
import lv.n3o.aoc2019.tasks.cleanLines

typealias C = C2

@Suppress("unused")
class Task : Task() {
    val data by lazy {
        input
            .cleanLines
            .mapIndexed { y, line ->
                line.mapIndexed { x, cell ->
                    C(x, y) to (cell == '#')
                }
            }
            .flatten()
            .toMap()
    }

    val asteroids by lazy { data.filter { (_, v) -> v }.keys }

    val visibilityMap by lazy {
        asteroids.associateWith { asteroid ->
            asteroids.filter { c ->
                c.linePoints(asteroid).filter { data[it] == true }.count() == 2
            }.size
        }
    }

    val base by lazy {
        visibilityMap.maxBy { it.value }?.key ?: error("Missing base")
    }

    override fun a(): String {
        return "${visibilityMap.maxBy { it.value }?.value}"
    }

    override fun b(): String {
        return sequence {
            val asteroidField = data.toMutableMap()
            while (true) {
                asteroidField
                    .asSequence()
                    .filter { (_, v) -> v }
                    .filter { (k, _) -> base.linePoints(k).filter { asteroidField[it] == true }.count() == 2 }
                    .map { (k, _) -> k }
                    .sortedBy { base.clockAngle(it) }
                    .forEach {
                        yield(it)
                        asteroidField[it] = false
                    }
            }
        }.drop(199).first().let {
            "${it.x * 100 + it.y}"
        }

    }
}