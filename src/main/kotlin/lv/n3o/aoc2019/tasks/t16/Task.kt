package lv.n3o.aoc2019.tasks.t16

import lv.n3o.aoc2019.tasks.Task
import kotlin.math.absoluteValue

@Suppress("unused")
class Task : Task() {
    val data by lazy { input.toList().map { it.toString().toInt() } }

    override fun a(): String {
        val basePattern = listOf(0, 1, 0, -1).toIntArray()
        val patterns = Array(data.size) { position ->
            sequence {
                while (true) {
                    for (n in basePattern) {
                        for (i in 0..position) {
                            yield(n)
                        }
                    }
                }
            }
                .drop(1)
                .take(data.size)
                .toList()
                .toIntArray()
        }

        fun processSignal(signal: IntArray) {
            IntArray(signal.size) { d ->
                val selectedPattern = patterns[d]
                var sum = 0
                for (i in signal.indices) {
                    sum += signal[i] * selectedPattern[i]
                }
                sum.absoluteValue % 10
            }.copyInto(signal)
        }

        val signal = data.toIntArray()

        for (i in 0 until 100) {
            processSignal(signal)
        }

        return signal.take(8).joinToString("")
    }

    override fun b(): String {
        val offset = data.take(7).reduce { acc, x -> acc * 10 + x }

        val newSize = data.size * 10000

        if (offset * 2 < newSize) error("This approach will not work :(")

        val reversedData = data.reversed().toIntArray()

        val signal = IntArray(newSize - offset) {
            reversedData[it % reversedData.size]
        }

        for (t in 0 until 100) {
            var sum = 0
            for (i in signal.indices) {
                sum += signal[i]
                signal[i] = sum % 10
            }
        }

        return signal.takeLast(8).reversed().joinToString("")
    }
}
