package lv.n3o.aoc2019.tasks

import kotlin.math.roundToInt

val String.cleanLines get() = lines().map { it.trim() }.filter { it.isNotBlank() }

fun <T> List<T>.infinite() = sequence {
    while (true) {
        yieldAll(this@infinite)
    }
}


