package lv.n3o.aoc2019.tasks.t03

import lv.n3o.aoc2019.coords.C2
import lv.n3o.aoc2019.tasks.Task

typealias C = C2

@Suppress("unused")
class Task : Task() {
    private val lines by lazy { input.lines().map { it.split(",") } }

    private val field: Map<C, IntArray> by lazy {
        val field = mutableMapOf<C, IntArray>()
        lines.forEachIndexed { lineNumber, line ->
            var moveCounter = 0
            var c = initial
            line.forEach { rule ->
                field.putIfAbsent(c, intArrayOf(-1, -1))
                val direction = fromDirectionChar(rule[0])
                val amount = rule.substring(1).toInt()
                repeat(amount) {
                    c += direction
                    moveCounter++
                    if (field[c] == null) {
                        field[c] = intArrayOf(-1, -1)
                    }
                    if (field[c]?.get(lineNumber) == -1) {
                        field[c]?.set(lineNumber, moveCounter)
                    }
                }
            }
        }
        field
    }

    fun fromDirectionChar(c: Char) = when (c) {
        'R' -> C.right
        'L' -> C.left
        'U' -> C.up
        'D' -> C.down
        else -> error("Unknown direction $c")
    }

    val initial = C(0, 0)

    override fun a() =
        "${field.mapNotNull { (k, v) -> if (v[0] > 0 && v[1] > 0) k.distance(initial) else null }.min()}"

    override fun b() = "${field.values.filter { v -> v[0] > 0 && v[1] > 0 }.map(IntArray::sum).first()}"
}
