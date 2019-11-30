package lv.n3o.aoc2019.tasks

import kotlin.math.roundToInt

val String.cleanLines get() = lines().map { it.trim() }.filter { it.isNotBlank() }
private val startTime = System.nanoTime()
val timeFromApplicationStart get() = System.nanoTime() - startTime

fun Long.formatTime(): String = (this / 1000000.0).roundToInt().toString()
    .padStart(6, ' ')
    .chunked(3)
    .joinToString(" ") + "ms"