package lv.n3o.aoc2019.coords

import kotlin.math.abs

class C2(val x: Int, val y: Int) {
    companion object {
        val right = C2(1, 0)
        val left = C2(-1, 0)
        val up = C2(0, -1)
        val down = C2(0, 1)
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
