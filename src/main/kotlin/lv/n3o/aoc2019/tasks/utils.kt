package lv.n3o.aoc2019.tasks

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