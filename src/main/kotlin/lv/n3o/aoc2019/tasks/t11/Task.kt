package lv.n3o.aoc2019.tasks.t11

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import lv.n3o.aoc2019.coords.C
import lv.n3o.aoc2019.coords.Coord2d
import lv.n3o.aoc2019.coords.minus
import lv.n3o.aoc2019.coords.rotate
import lv.n3o.aoc2019.ocr.recognizeLetters
import lv.n3o.aoc2019.tasks.IntComp
import lv.n3o.aoc2019.tasks.Memory
import lv.n3o.aoc2019.tasks.Task

@Suppress("unused")
class Task : Task() {
    private val programNumbers by lazy { input.split(",").map(String::toLong) }

    fun field(initial: Long): Map<C, Boolean> {
        val field = mutableMapOf<C, Boolean>()
        runBlocking {
            val memory = Memory(programNumbers.toMutableList())
            val input = Channel<Long>(2)
            val output = Channel<Long>()
            launch {
                var current = C(0, 0)
                var direction: Coord2d = C(0, -1)
                input.send(initial)
                var state = true
                for (o in output) {
                    if (state) {
                        // Paint
                        field[current] = o == 1L
                    } else {
                        // Move
                        direction = direction.rotate(o == 1L)
                        current += direction
                        input.send(if (field[current] == true) 1 else 0)
                    }
                    state = !state
                }
            }
            IntComp(memory, input, output).runToHalt()
        }
        return field
    }

    override fun a(): String {
        return "${field(0L).keys.size}"
    }

    override fun b(): String {
        val interestingField = field(1L).filter { (_, v) -> v }
        val shift = C(0, 0) - interestingField.keys.reduce { a, b -> C(a.x.coerceAtMost(b.x), a.y.coerceAtMost(b.y)) }
        val shiftedField = interestingField.mapKeys { (k, _) -> k + shift }
        val max = interestingField.keys.reduce { a, b -> C(a.x.coerceAtLeast(b.x), a.y.coerceAtLeast(b.y)) }
        val letters = (0..max.y).map { y ->
            (0..max.x).map { x ->
                if (shiftedField[C(x, y)] == true) '1' else '0'
            }
        }
        return letters.recognizeLetters()
    }
}