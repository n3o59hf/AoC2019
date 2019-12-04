package lv.n3o.aoc2019.tasks.t04

import lv.n3o.aoc2019.tasks.Task
import lv.n3o.aoc2019.tasks.cleanLines
import javax.swing.text.StyledEditorKit

@Suppress("unused")
class Task : Task() {
    private val range by lazy { data04.split("-").map { it.toInt() }.let { (a, b) -> a..b } }

    private val numbers by lazy {
        range.map { Password(it) }
            .filter { it.increasing }
    }

    override fun a() = "${numbers.count { it.repeats.any { r -> r >= 2 } }}"

    override fun b() = "${numbers.count { it.repeats.any { r -> r == 2 } }}"
}

inline class Password(val password: Int) {
    val increasing: Boolean
        get() {
            var p = password
            while (p > 0) {
                if (p % 10 < (p % 100) / 10) return false
                p /= 10
            }
            return true
        }

    val numbers: Sequence<Int>
        get() = sequence {
            var p = password
            while (p > 0) {
                yield(p % 10)
                p /= 10
            }
        }

    val repeats: Sequence<Int>
        get() = sequence {
            val seq = numbers.iterator()
            var count = 1
            var current = seq.next()

            while (seq.hasNext()) {
                val next = seq.next()
                if (next != current) {
                    yield(count)
                    count = 0
                    current = next
                }
                count++
            }
            yield(count)
        }
}