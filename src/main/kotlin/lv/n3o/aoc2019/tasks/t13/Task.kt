package lv.n3o.aoc2019.tasks.t13

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import lv.n3o.aoc2019.coords.*
import lv.n3o.aoc2019.tasks.*
import lv.n3o.aoc2019.tasks.Task

typealias C = C2

@ExperimentalCoroutinesApi
@Suppress("unused")
class Task : Task() {
    private val programNumbers by lazy { input.split(",").map(String::toLong) }

    override fun a() = doComputation(programNumbers).windowed(3, 3).count { it[2] == 2L }.toString()

    override fun b(): String {
        var score = 0L
        runBlocking {
            val memory = Memory(programNumbers.toMutableList())
            memory[Mode.IMMEDIATE, 0] = 2L
            var ball = C(0, 0)
            var ballDirection: Coord2d = C(0, 0)
            var paddle = 0
            val display = mutableMapOf<Coord2d, Long>()
            val joystickController = Channel<Long>(1)
            val displayController = Channel<Long>(Channel.RENDEZVOUS)
            val displayOutput = Channel<Pair<C, Long>>(Channel.RENDEZVOUS)
            val lastDestroyed = mutableSetOf<C2>()
            launch {
                val receiveQueue = mutableListOf<Long>()
                for (i in displayController) {
                    receiveQueue.add(i)
                    if (receiveQueue.size == 3) {
                        val (x, y, tile) = receiveQueue
                        receiveQueue.clear()
                        displayOutput.send(C2(x.toInt(), y.toInt()) to tile)
                    }
                }
                displayOutput.close()
            }

            launch {
                for (t in displayOutput) {
                    if (t.first.x == -1) {
                        score = t.second
                        draw(display, ball - ballDirection, score)
                    } else {
                        if (display[t.first] == 2L && t.second == 0L) {
                            lastDestroyed.add(t.first)
                        }
                        display[t.first] = t.second
                        if (t.second == 3L) {
                            paddle = t.first.x
                        } else if (t.second == 4L) {
                            ballDirection = ball.vector(t.first).unit()
                            ball = t.first
                            draw(display, ball - ballDirection, score)
                        }
                    }
                }
                displayController.close()
            }

            IntComp(memory, output = displayController) {
                val verticalReflect = display[ball + ballDirection.new(x = 0)] == 1L ||
                        lastDestroyed.contains(ball + ballDirection.new(x = 0))

                val wallReflect = display[ball + ballDirection.new(y = 0)] == 1L
                val cornerReflect = lastDestroyed.contains(ball + ballDirection)
                val sideReflect = lastDestroyed.contains(ball + ballDirection.new(y = 0))
                val oppositeCornerBackReflect =
                    lastDestroyed.contains(ball + ballDirection.new(x = -ballDirection.x))
                val backCornerReflect = verticalReflect &&
                        lastDestroyed.contains(ball + ballDirection.new(y = -ballDirection.y))


                val ballReflect =
                    wallReflect || cornerReflect || (sideReflect && !oppositeCornerBackReflect) || backCornerReflect

                val direction = if (ballReflect) -ballDirection.x else ballDirection.x

                val input = when {
                    ball.x + direction < paddle -> -1L
                    ball.x + direction > paddle -> 1L
                    else -> 0L
                }
                lastDestroyed.clear()
                input
            }.runToHalt()

            joystickController.close()
        }
        return "$score"
    }


    private fun draw(display: MutableMap<Coord2d, Long>, ballShade: Coord2d, score: Long) {
        if (!isLoggerOn()) return
        val maxX = display.keys.map { it.x }.max() ?: 1
        val maxY = display.keys.map { it.y }.max() ?: 1

        val output = "\n" + (0..maxY).map { y ->
            (0..maxX).map { x ->
                when (display[C(x, y)]?.toInt()) {
                    1 -> '░'
                    2 -> '█'
                    3 -> '▔'
                    4 -> '▣'
                    else -> if (C(x, y) == ballShade) '□' else ' '
                }
            }.joinToString("")

        }.joinToString("\n") + "\nScore: $score\n\n"

        log(output)
    }

}
