package lv.n3o.aoc2019.tasks.t07

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import lv.n3o.aoc2019.tasks.*
import lv.n3o.aoc2019.tasks.Task

@Suppress("unused")
class Task : Task() {
    private val programNumbers by lazy { input.split(",").map(String::toLong) }

    override fun a(): String {
        fun amplifier(phase: Long, input: Long): Long {
            return doComputation(programNumbers, phase, input).first()
        }

        val max = listOf(0L, 1L, 2L, 3L, 4L).permute().map {
            it.fold(0L) { input, phase ->
                val output = amplifier(phase, input)
                output

            }
        }.max()

        return "$max"
    }

    override fun b(): String {
        fun amplifiers(phases: List<Long>): Long {

            fun amplifier(input: Channel<Long>, output: Channel<Long>) {
                val memory = Memory(programNumbers)
                val comp = IntComp(memory, input, output)
                GlobalScope.launch { comp.runToHalt() }
            }

            return runBlocking {
                val c1 = Channel<Long>(2)
                val c2 = Channel<Long>(1)
                val c3 = Channel<Long>(1)
                val c4 = Channel<Long>(1)
                val c5 = Channel<Long>(1)
                c1.send(phases[0])
                c1.send(0)
                c2.send(phases[1])
                c3.send(phases[2])
                c4.send(phases[3])
                c5.send(phases[4])

                val output = Channel<Long>()

                amplifier(c1, c2)
                amplifier(c2, c3)
                amplifier(c3, c4)
                amplifier(c4, c5)
                amplifier(c5, output)

                var last = 0L
                for (i in output) {
                    last = i
                    c1.send(i)
                }
                last
            }
        }

        return "${listOf(5L, 6L, 7L, 8L, 9L).permute().map { amplifiers(it) }.max()}"
    }
}