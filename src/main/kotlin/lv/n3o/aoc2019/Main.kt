package lv.n3o.aoc2019

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import lv.n3o.aoc2019.tasks.Task
import java.io.OutputStream
import java.io.PrintWriter
import kotlin.math.roundToInt
import kotlin.system.measureNanoTime

private const val DEBUGGING = false

object Main : CoroutineScope by CoroutineScope(Dispatchers.Default) {
    @JvmStatic
    fun main(args: Array<String>) {
        runBlocking {
            val taskList = mutableListOf<Pair<String, Task>>()
            var totalTime: Long = 0
            val preparationTime = measureNanoTime {
                (1..25).mapNotNull { index ->
                    async {
                        val number = index.toString().padStart(2, '0')
                        try {
                            val clazz = Class.forName("lv.n3o.aoc2019.tasks.t$number.Task")
                            val task = clazz.getConstructor().newInstance() as? Task
                            task?.let { it -> number to it }
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

            taskList.map { (name, task) ->
                async {
                    var taskTime: Long = 0
                    buildString {
                        try {
                            if (DEBUGGING) task.debugListener = { append(it) }
                            var partA = ""
                            var partB = ""
                            val partATime = measureNanoTime { partA = task.a() }
                            val partBTime = measureNanoTime { partB = task.b() }
                            append("=== DAY $name === Time: ${(partATime + partBTime).formatTime()}\n")
                            append("\t Part A: ${partATime.formatTime()} - ${partA}\n")
                            append("\t Part B: ${partBTime.formatTime()} - ${partB}\n")
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

