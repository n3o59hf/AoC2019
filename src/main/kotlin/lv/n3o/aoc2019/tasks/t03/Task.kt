package lv.n3o.aoc2019.tasks.t03

import lv.n3o.aoc2019.tasks.Task
import kotlin.math.abs

@Suppress("unused")
class Task : Task() {
    class C2(val x: Int, val y: Int) {
        companion object {
            val right = C2(1,0)
            val left = C2(-1,0)
            val up = C2(0,-1)
            val down = C2(0,1)
        }
        operator fun plus(other: C2) = C2(x + other.x, y + other.y)
        fun distance(to: C2) = abs(x - to.x) + abs(y - to.y)
        override fun equals(other: Any?): Boolean {
            if (other !is C2) return false

            if (x != other.x) return false
            if (y != other.y) return false

            return true
        }

        override fun hashCode(): Int {
            var result = x
            result = 31 * result + y
            return result
        }

    }

    val lines = data03.lines().map { it.split(",") }

    fun fromDirectionChar(c: Char) = when(c) {
        'R' -> C2.right
        'L' -> C2.left
        'U' -> C2.up
        'D' -> C2.down
        else -> error("Unknown direction $c")
    }

    val initial = C2(0, 0)

    override fun a(): String {
        val field = mutableMapOf<C2, Int>()
        lines.forEachIndexed { lineNumber, line ->
            val bitMask = 1 shl lineNumber
            var c = initial
            line.forEach { rule ->
                field[c] = (field[c] ?: 0) or bitMask

                val direction = fromDirectionChar(rule[0])
                val amount = rule.substring(1).toInt()
                repeat(amount) {
                    c += direction
                    field[c] = (field[c] ?: 0) or bitMask
                }
            }
        }

        val nearestIntersection = field.mapNotNull {  (k, v) -> if (v == 3) k.distance(initial) else null }.sorted()[1]

        return nearestIntersection.toString()

    }

    override fun b(): String {
        val field = mutableMapOf<C2, MutableList<Int>>()
        lines.forEachIndexed { lineNumber, line ->
            var moveCounter = 0
            var c = initial
            line.forEach { rule ->
                field.putIfAbsent(c, mutableListOf(0, 0))
                val direction = fromDirectionChar(rule[0])
                val amount = rule.substring(1).toInt()
                repeat(amount) {
                    c += direction
                    moveCounter++
                    if (field[c] == null) {
                        field[c] = mutableListOf(-1,-1)
                    }
                    if (field[c]?.get(lineNumber) == -1) {
                        field[c]?.set(lineNumber, moveCounter)
                    }
                }
            }
        }

        val nearestIntersection = field.values.filter { v -> v[0] > 0 && v[1] > 0 }.map(MutableList<Int>::sum).first()

        return nearestIntersection.toString()
    }
}