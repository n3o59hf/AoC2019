package lv.n3o.aoc2019.coords

private const val COORD_OFFSET = 32768
private const val MAX_COORD2 = COORD_OFFSET * 2

inline class CX(val coord: Int) : Coord {
    companion object {
        val right = CX(1, 0)
        val left = CX(-1, 0)
        val up = CX(0, -1)
        val down = CX(0, 1)
    }

    constructor(x: Int, y: Int) : this((x + COORD_OFFSET) + (y + COORD_OFFSET) * MAX_COORD2)

    override operator fun plus(other: Coord) = CX(x + other.x, y + other.y)

    override val x: Int get() = (coord % MAX_COORD2) - COORD_OFFSET
    override val y: Int get() = (coord / MAX_COORD2) - COORD_OFFSET

    override fun new(x: Int, y: Int) = CX(x, y)
}
