package lv.n3o.aoc2019.tasks

import lv.n3o.aoc2019.coords.C
import lv.n3o.aoc2019.coords.Coord2d
import kotlin.math.abs

val String.cleanLines get() = lines().map { it.trim() }.filter { it.isNotBlank() }

fun <T> List<T>.infinite() = sequence {
    while (true) {
        yieldAll(this@infinite)
    }
}

fun <T> List<T>.permute(): List<List<T>> {
    if (size == 1) return listOf(this)

    val permutations = mutableListOf<List<T>>()
    val movableElement = first()
    for (p in drop(1).permute())
        for (i in 0..p.size) {
            val mutation = p.toMutableList()
            mutation.add(i, movableElement)
            permutations.add(mutation)
        }
    return permutations
}

fun gcd(a: Int, b: Int): Int {
    var gcd = a.coerceAtMost(b)
    while (gcd > 0) {
        if (a % gcd == 0 && b % gcd == 0)
            return gcd
        gcd--
    }
    return -1
}

fun gcd(a: Long, b: Long): Long {
    var gcd = a.coerceAtMost(b)
    while (gcd > 0L) {
        if (a % gcd == 0L && b % gcd == 0L)
            return gcd
        gcd--
    }
    return -1
}

fun lcm(a: Long, b: Long): Long = abs(a * b) / gcd(a, b)

fun Long.divRoundedUp(divider: Long) = this.toNearestMultipleUp(divider) / divider

fun Long.toNearestMultipleUp(factor: Long): Long {
    val reminder = if (this % factor > 0) 1 else 0
    return ((this / factor) + reminder) * factor
}

fun <T> Map<Coord2d, T>.debugDraw(cellWidth: Int = 1, conversion: (T?) -> Any = { it.toString() }) {
    val allKeys = keys

    val maxX = allKeys.map(Coord2d::x).max() ?: 1
    val maxY = allKeys.map(Coord2d::y).max() ?: 1
    val minX = allKeys.map(Coord2d::x).min() ?: 1
    val minY = allKeys.map(Coord2d::y).min() ?: 1


    val cellBorder = (0 until cellWidth).joinToString("") { "-" }
    val verticalSeperator = "\n" + (minX..maxX).joinToString("+", "+", "+") { cellBorder } + "\n"

    val output = "\n$verticalSeperator" + (minY..maxY).map { y ->
        (minX..maxX).map { x ->
            var cell = conversion(this[C(x, y)]).toString()
            cell = cell.substring(0, cell.length.coerceAtMost(cellWidth))
            if (cell.length < cellWidth)
                cell = cell.padEnd(cell.length + ((cellWidth - cell.length) / 2))
            if (cell.length < cellWidth)
                cell = cell.padStart(cellWidth)
            cell
        }.joinToString("|", "|", "|")

    }.joinToString(verticalSeperator) + verticalSeperator
    println("\n$output\n")
}

class MapWithLazy<K, V>(val backingMap: MutableMap<K, V>, val lazy: (K) -> V) : MutableMap<K,V> by backingMap {
    override operator fun get(key: K): V {
        val value = backingMap[key]
        return if (value == null) {
            val lazyValue = lazy(key)
            backingMap[key] = lazyValue
            lazyValue
        } else {
            value
        }
    }
}

fun <K, V> MutableMap<K, V>.withLazy(lazy: (K) -> V) = MapWithLazy(this, lazy)