package lv.n3o.aoc2019.tasks.t20

import lv.n3o.aoc2019.coords.C2
import lv.n3o.aoc2019.coords.Coord2d
import lv.n3o.aoc2019.coords.neighbors4
import lv.n3o.aoc2019.tasks.Task
import lv.n3o.aoc2019.tasks.cleanLines
import lv.n3o.aoc2019.tasks.debugDraw

@Suppress("unused")
class Task : Task() {
    val data: Map<Coord2d, Char> by lazy {
        input
            .lines()
            .mapIndexed { y, line ->
                line.mapIndexed { x, c ->
                    C2(x, y) as Coord2d to c
                }
            }
            .flatten()
            .toMap()
    }

    val walkable: Set<Coord2d> by lazy {
        data.filter { it.value == '.' }.keys
    }

    val coordPortals by lazy {
        data
            .filter { it.value == '.' }
            .filter { it.key.neighbors4().any { neighbor -> data[neighbor]?.isLetter() == true } }
            .map { (coord, _) ->
                val firstLetter = coord.neighbors4().first { data[it]?.isLetter() == true }
                val secondLetter = firstLetter.neighbors4().first { data[it]?.isLetter() == true }
                val name = listOf(firstLetter, secondLetter).sortedBy { it.x }.sortedBy { it.y }
                    .joinToString("") { "${data[it]}" }
                coord to name
            }
            .toMap()
    }

    val minX by lazy { walkable.map { it.x }.toSet().min() ?: error("At least one walkable title needed") }
    val maxX by lazy { walkable.map { it.x }.toSet().max() ?: error("At least one walkable title needed") }
    val minY by lazy { walkable.map { it.y }.toSet().min() ?: error("At least one walkable title needed") }
    val maxY by lazy { walkable.map { it.y }.toSet().max() ?: error("At least one walkable title needed") }

    fun portalCoords(name: String, from: Coord2d? = null) =
        coordPortals.filter { it.value == name && it.key != from }.toList().firstOrNull()?.first

    fun isPortalOuther(portal: Coord2d) = (portal.x == minX || portal.x == maxX) || (portal.y == minY || portal.y == maxY)

    override fun a(): String {

        val start = portalCoords("AA") ?: error("No AA defined")
        val stepsMap = mutableMapOf(start to 0)
        val toCheck = mutableSetOf(start)

        while (toCheck.isNotEmpty()) {
            val current = toCheck.first()
            val currentSteps = stepsMap[current] ?: error("Steps should be already defined")
            toCheck.remove(current)
            val portal = coordPortals[current]

            val neighborsWorthVisiting = (current.neighbors4() + portal?.let { portalCoords(it, current) })
                .filterNotNull()
                .filter { walkable.contains(it) }
                .filter { (stepsMap[it] ?: Int.MAX_VALUE) > currentSteps + 1 }
            neighborsWorthVisiting.forEach {
                stepsMap[it] = currentSteps + 1
                toCheck.add(it)
            }
        }
        return stepsMap[portalCoords("ZZ")].toString()
    }

    override fun b(): String {
        val start = portalCoords("AA") ?: error("No AA defined")
        val stepsMap = mutableMapOf((start to 0) to 0)
        val toCheck = mutableListOf((start to 0))

        while (toCheck.isNotEmpty()) {
            val (current, depth) = toCheck.minBy { it.second } ?: error("Was not empty")
            val currentSteps = stepsMap[(current to depth)] ?: error("Steps should be already defined")
            toCheck.remove((current to depth))

            val neighborsWorthVisiting = current.neighbors4()
                .filter { walkable.contains(it) }
                .filter { (stepsMap[it to depth] ?: Int.MAX_VALUE) > currentSteps + 1 }
            neighborsWorthVisiting.forEach {
                stepsMap[it to depth] = currentSteps + 1
                toCheck.add(it to depth)
            }

            val portal = coordPortals[current]
            if (portal != null) {
                if (portal == "ZZ" && depth == 0) break
                val outher = isPortalOuther(current)
                val exit = portalCoords(portal, current)
                if (exit == null || (depth == 0 && outher)) continue
                val nextDepth = depth + if (outher) -1 else 1
                if ((stepsMap[exit to nextDepth] ?: Int.MAX_VALUE) > currentSteps + 1) {
                    stepsMap[exit to nextDepth] = currentSteps + 1
                    toCheck.add(exit to nextDepth)
                }
            }
        }

        return stepsMap[portalCoords("ZZ") to 0].toString()
    }
}