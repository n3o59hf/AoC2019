package lv.n3o.aoc2019.coords

import kotlin.math.abs

interface Coord3d {
    val x: Int
    val y: Int
    val z: Int

    operator fun plus(other: Coord3d): Coord3d
    fun new(x: Int, y: Int, z: Int): Coord3d
}

operator fun Coord3d.minus(other: Coord3d) = this + new(-other.x, -other.y, -other.z)
fun Coord3d.distance(to: Coord3d) = abs(to.x - x) + abs(to.y - y) + abs(to.z - z)

fun Coord3d.vector(to: Coord3d) = new(to.x - x, to.y - y, to.z - z)
fun Coord3d.unit() = new(
    when {
        x > 0 -> 1
        x < 0 -> -1
        else -> 0
    },
    when {
        y > 0 -> 1
        y < 0 -> -1
        else -> 0
    },
    when {
        z > 0 -> 1
        z < 0 -> -1
        else -> 0
    }

)


