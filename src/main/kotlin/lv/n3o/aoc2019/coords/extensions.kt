package lv.n3o.aoc2019.coords

import lv.n3o.aoc2019.tasks.gcd
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2

typealias C = C2

interface Coord {
    val x: Int
    val y: Int

    operator fun plus(other: Coord): Coord
    fun new(x: Int, y: Int): Coord
}

operator fun Coord.minus(other: Coord) = this + new(-other.x, -other.y)
fun Coord.distance(to: Coord) = abs(to.x - x) + abs(to.y - y)

fun Coord.vector(to: Coord) = new(to.x - x, to.y - y)
fun Coord.linePoints(to: Coord) = sequence {
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

fun Coord.clockAngle(to: Coord): Double {
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

fun Coord.rotate(direction: Boolean) = if (direction) rotateRight() else rotateLeft()

fun Coord.rotateRight() = new(-y, x)
fun Coord.rotateLeft() = new(y, -x)
