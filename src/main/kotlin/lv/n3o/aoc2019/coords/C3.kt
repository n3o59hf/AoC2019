package lv.n3o.aoc2019.coords

data class C3(
    override val x: Int,
    override val y: Int,
    override val z: Int
) : Coord3d {
    override operator fun plus(other: Coord3d) = new(x + other.x, y + other.y, z + other.z)
    override fun new(x: Int, y: Int, z: Int) = C3(x, y, z)
}