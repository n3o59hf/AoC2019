package lv.n3o.aoc2019.coords

class C2(override val x: Int, override val y: Int) : Coord2d {
    override operator fun plus(other: Coord2d) = C2(x + other.x, y + other.y)

    override fun equals(other: Any?): Boolean {
        if (other !is C2) return false

        if (x != other.x) return false
        if (y != other.y) return false

        return true
    }

    override fun new(x: Int, y: Int) = C2(x, y)

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        return result
    }

    override fun toString(): String {
        return "C2($x, $y)"
    }
}
