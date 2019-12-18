package lv.n3o.aoc2019.tasks.t17

import lv.n3o.aoc2019.coords.C2
import lv.n3o.aoc2019.coords.Coord2d
import lv.n3o.aoc2019.coords.neighbors4
import lv.n3o.aoc2019.coords.rotate
import lv.n3o.aoc2019.tasks.Task
import lv.n3o.aoc2019.tasks.cleanLines
import lv.n3o.aoc2019.tasks.doComputation

@Suppress("unused")
class Task : Task() {
    private val programNumbers by lazy { input.split(",").map(String::toLong) }
    lateinit var robotStart: Coord2d
    val map: String by lazy { doComputation(programNumbers).map { it.toChar() }.joinToString("") }
    val scaffolds by lazy {
        mutableMapOf<Coord2d, Boolean>().apply {
            map.cleanLines.forEachIndexed { y, line ->
                line.forEachIndexed { x, cell ->
                    this[C2(x, y)] = cell != '.'
                    if (cell != '.' && cell != '#' && cell != '\n') robotStart = C2(x, y)
                }
            }

        }
    }

    override fun a() = scaffolds
        .filter { it.value }
        .keys
        .filter { it.neighbors4().all { c -> scaffolds[c] == true } }
        .map { it.x * it.y }
        .sum()
        .toString()

    override fun b(): String {
        val robotSign = map.first { it != '.' && it != '#' && it != '\n' }
        var robot = robotStart
        var direction: Coord2d = when (robotSign) {
            '^' -> C2(0, -1)
            '>' -> C2(1, 0)
            'v' -> C2(0, 1)
            '<' -> C2(-1, 0)
            else -> error("Unknown robot direction $robotSign")
        }

        val movements = mutableListOf<String>()
        pathfinding@ while (true) {
            when {
                // Move forward if there is scaffold
                scaffolds[robot + direction] == true -> {
                    movements.add("1")
                    robot += direction
                }
                // Rotate left
                scaffolds[robot + direction.rotate(false)] == true -> {
                    movements.add("L")
                    direction = direction.rotate(false)
                }
                // Rotate right
                scaffolds[robot + direction.rotate(true)] == true -> {
                    movements.add("R")
                    direction = direction.rotate(true)
                }
                // At the end of line
                else -> break@pathfinding
            }
        }

        val compactMovements = movements.fold(listOf<String>()) { acc, s ->
            if (s.toIntOrNull() != null && acc.lastOrNull()?.toIntOrNull() != null) {
                acc.dropLast(1) + "${acc.last().toInt() + s.toInt()}"
            } else {
                acc + s
            }
        }.windowed(2, 2).map { "${it[0]}.${it[1]}" }.joinToString(",")

        val validCombos = mutableListOf<List<String>>()
        val compactMovementParts = compactMovements.split(",")
        for (a in 1..compactMovementParts.size - 2) {
            val partA = compactMovementParts.take(a).joinToString(",")
            val compactMovementPartsWithoutA =
                compactMovements.replace(partA, "A")
                    .replace("A,", "")
                    .replace("A", "")
                    .split(",")
            for (b in 1 until compactMovementPartsWithoutA.size) {
                val partB = compactMovementPartsWithoutA.take(b).joinToString(",")
                val compactMovementPartsWithoutB = compactMovements
                    .replace(partA, "A")
                    .replace("A,", "")
                    .replace("A", "")
                    .replace(partB, "B")
                    .replace("B,", "")
                    .replace("B", "")
                    .split(",")
                for (c in 1..compactMovementPartsWithoutB.size) {
                    val partC = compactMovementPartsWithoutB.take(c).joinToString(",")
                    val replacement = compactMovements.replace(partA, "A").replace(partB, "B").replace(partC, "C")
                    if (replacement.all { it == 'A' || it == 'B' || it == 'C' || it == ',' }) {
                        validCombos.add(listOf(partA, partB, partC))
                    }
                }
            }
        }
        val (partA, partB, partC) = validCombos.filter { it.all { it.length <= 20 } }.first()
        val sequence = compactMovements.replace(partA, "A").replace(partB, "B").replace(partC, "C")

        val instructions = buildString {
            append(sequence)
            append('\n')
            append(partA.replace(".", ","))
            append('\n')
            append(partB.replace(".", ","))
            append('\n')
            append(partC.replace(".", ","))
            append('\n')
            append("n\n")
        }


        return doComputation(
            listOf(2L) + programNumbers.drop(1),
            *instructions.toByteArray().map { it.toLong() }.toLongArray()
        ).last().toString()
    }
}