package lv.n3o.aoc2019.coords

import lv.n3o.aoc2019.tasks.gcd
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2

typealias C = C2

interface Coord2d {
    val x: Int
    val y: Int

    operator fun plus(other: Coord2d): Coord2d
    fun new(x: Int = this.x, y: Int = this.y): Coord2d
}

operator fun Coord2d.minus(other: Coord2d) = this + new(-other.x, -other.y)
fun Coord2d.distance(to: Coord2d) = abs(to.x - x) + abs(to.y - y)

fun Coord2d.vector(to: Coord2d) = new(to.x - x, to.y - y)
fun Coord2d.linePoints(to: Coord2d) = sequence {
    if (this@linePoints == to) {
        yield(this@linePoints)
        return@sequence
    }

    val dx = to.x - x
    val dy = to.y - y

    var step = new(0, 0)
    if (dx == 0 || dy == 0) {
        when {
            dy > 0 -> step = new(0, 1)
            dy < 0 -> step = new(0, -1)
            dx > 0 -> step = new(1, 0)
            dx < 0 -> step = new(-1, 0)
        }
    } else {
        val divider = gcd(abs(dx), abs(dy))
        if (divider < 1) {
            yield(this)
            yield(to)
            return@sequence
        }
        step = new(dx / divider, dy / divider)
    }

    var current = this@linePoints
    yield(current)
    while (current != to) {
        current += step
        yield(current)
    }
}

fun Coord2d.clockAngle(to: Coord2d): Double {
    val vector = vector(to)
    return when {
        vector.x == 0 && vector.y < 0 -> 0.0
        vector.x == 0 && vector.y > 0 -> 0.5
        vector.y == 0 && vector.x > 0 -> 0.25
        vector.y == 0 && vector.x < 0 -> 0.75
        else -> {
            var clockDial = 0.5 - atan2(vector.x.toDouble(), vector.y.toDouble()) / (2 * PI)
            if (clockDial >= 1.0) clockDial -= 1.0
            if (clockDial < 0) clockDial += 1.0
            clockDial
        }
    }
}

fun Coord2d.rotate(direction: Boolean) = if (direction) rotateRight() else rotateLeft()

fun Coord2d.rotateRight() = new(-y, x)
fun Coord2d.rotateLeft() = new(y, -x)

fun Coord2d.unit() = new(
    x.coerceIn(-1, 1),
    y.coerceIn(-1, 1)
)

fun Coord2d.neighbors4() = listOf(
    this + this.new(0, -1),
    this + this.new(1, 0),
    this + this.new(0, 1),
    this + this.new(-1, 0)
)