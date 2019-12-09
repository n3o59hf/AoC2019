package lv.n3o.aoc2019.coords

import kotlin.math.abs

private const val COORD_OFFSET = 32768
private const val MAX_COORD2 = COORD_OFFSET * 2

inline class CX(val coord: Int) {
    companion object {
        val right = CX(1, 0)
        val left = CX(-1, 0)
        val up = CX(0, -1)
        val down = CX(0, 1)
    }

    constructor(x: Int, y: Int) : this((x + COORD_OFFSET) + (y + COORD_OFFSET) * MAX_COORD2)

    operator fun plus(other: CX) = CX(x + other.x, y + other.y)

    val x: Int get() = (coord % MAX_COORD2) - COORD_OFFSET
    val y: Int get() = (coord / MAX_COORD2) - COORD_OFFSET
    fun distance(to: CX) = abs(x - to.x) + abs(y - to.y)

}
