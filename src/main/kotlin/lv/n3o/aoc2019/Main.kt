package lv.n3o.aoc2019

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import lv.n3o.aoc2019.tasks.IO
import lv.n3o.aoc2019.tasks.Task
import kotlin.math.roundToInt
import kotlin.system.measureNanoTime

private const val DEBUGGING = false

object Main : CoroutineScope by CoroutineScope(Dispatchers.Default) {
    @JvmStatic
    fun main(args: Array<String>) {
        runBlocking {
            val taskList = mutableListOf<Triple<String, Task, IO>>()
            var totalTime: Long = 0
            val preparationTime = measureNanoTime {
                (1..25).mapNotNull { index ->
                    async {
                        val number = index.toString().padStart(2, '0')
                        try {
                            val ioClass = Class.forName("lv.n3o.aoc2019.tasks.t$number.IO")
                            val io = ioClass.getConstructor().newInstance() as IO

                            val taskClass = Class.forName("lv.n3o.aoc2019.tasks.t$number.Task")
                            val task = taskClass.getConstructor().newInstance() as Task
                            task.input = io.input

                            Triple(number, task, io)
                        } catch (e: Exception) {
                            null
                        }
                    }
                }
                    .mapNotNull { it.await() }
                    .forEach {
                        taskList.add(it)
                    }
            }

            println("Preparation: ${preparationTime.formatTime()}")

            totalTime += preparationTime

            taskList.map { (name, task, io) ->
                async {
                    var taskTime: Long = 0
                    buildString {
                        try {
                            if (DEBUGGING) {
                                append("=== DAY $name === Debug\n")
                                task.debugListener = { append(it) }
                            }
                            var partA = ""
                            var partB = ""
                            val partATime = measureNanoTime { partA = task.a() }
                            val partBTime = measureNanoTime { partB = task.b() }
                            val partAResult =
                                if (io.testA == null) "Not specified" else if (io.testA == partA) "OK" else "FAIL (expected:${io.testA}"
                            val partBResult =
                                if (io.testB == null) "Not specified" else if (io.testB == partB) "OK" else "FAIL (expected:${io.testB}"

                            append("=== DAY $name === Time: ${(partATime + partBTime).formatTime()}\n")
                            append(
                                "\t Part A: ${partATime.formatTime()} - ${partA.padEnd(
                                    32,
                                    ' '
                                )} Result: $partAResult\n"
                            )
                            append(
                                "\t Part B: ${partBTime.formatTime()} - ${partB.padEnd(
                                    32,
                                    ' '
                                )} Result: $partBResult\n"
                            )
                            taskTime += partATime
                            taskTime += partBTime
                        } catch (e: Exception) {
                            append("=== DAY $name === FAILED!\n")
                            append("\t${e::class.java.simpleName} - ${e.message}\n")
                            e.stackTrace.forEach {
                                append("$it\n")
                            }
                        }
                    } to taskTime
                }
            }.forEach {
                val (text, time) = it.await()
                println(text)
                println()
                totalTime += time
            }

            println("Time total: ${totalTime.formatTime()}")
        }
    }
}

private val startTime = System.nanoTime()

val timeFromApplicationStart get() = System.nanoTime() - startTime

fun Long.formatTime(): String = (this / 1000000.0).roundToInt().toString()
    .padStart(6, ' ')
    .chunked(3)
    .joinToString(" ") + "ms"

