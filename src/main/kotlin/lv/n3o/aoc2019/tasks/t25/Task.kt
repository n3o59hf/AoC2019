package lv.n3o.aoc2019.tasks.t25

import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.yield
import lv.n3o.aoc2019.tasks.*
import lv.n3o.aoc2019.tasks.Task
import kotlin.properties.Delegates

@Suppress("unused")
class Task : Task() {
    private val programNumbers by lazy { input.split(",").map(String::toLong) }

    private val itemsBlacklist = setOf(
        "infinite loop",
        "molten lava",
        "photons",
        "giant electromagnet",
        "escape pod"
    )

    override fun a(): String {
        var stage = 0
        val currentRoute = mutableListOf<String>()
        val rooms = mutableSetOf<Room>()

        fun opposite(direction: String) = when (direction) {
            "north" -> "south"
            "south" -> "north"
            "east" -> "west"
            "west" -> "east"
            else -> error("Unexpected direction $direction")
        }

        var roomName = ""
        var directions: List<String> = listOf()
        var items: List<String> = listOf()
        var itemVariations: Iterator<Set<String>> by Delegates.notNull()
        val commandsToExecute = mutableListOf<String>()
        var i = 0
        val answer = asciiComputer(programNumbers) { input ->
            log { "$i\n===\n${input.joinToString("\n")}" }
            i++
            if (commandsToExecute.isNotEmpty()) {
                commandsToExecute.removeAt(0)
            } else {
                if (stage == 0) {
                    if (input.any { it.contains("ejected back to the checkpoint") }) currentRoute.removeAt(currentRoute.size - 1)
                    if (input[0][0] == '=') {
                        roomName = input[0].replace("== ", "").replace(" ==", "")
                        directions =
                            input.dropWhile { it != "Doors here lead:" }.drop(1).takeWhile { it.startsWith("-") }
                                .map { it.replace("- ", "") }
                        items = input.dropWhile { it != "Items here:" }.drop(1).takeWhile { it.startsWith("-") }
                            .map { it.replace("- ", "") }
                    }
                    log { "\n$currentRoute" }
                    val room = rooms.firstOrNull { it.name == roomName } ?: Room(
                        roomName,
                        directions,
                        items,
                        currentRoute.toList()
                    ).also { rooms.add(it) }

                    val takeableItems = items - itemsBlacklist
                    if (takeableItems.isNotEmpty()) {
                        items = items - takeableItems.first()
                        "take ${takeableItems.first()}"
                    } else {
                        if (currentRoute.isNotEmpty())
                            room.visitedDirections.add(opposite(currentRoute.last()))

                        val toVisit = room.directions - room.visitedDirections
                        if (toVisit.isEmpty()) {
                            if (currentRoute.isEmpty()) {
                                log { rooms.joinToString("\n") { "${it.name} => ${it.directions} ${it.items}" } }
                                stage = 1
                                commandsToExecute.addAll(rooms.first { it.name == "Pressure-Sensitive Floor" }.route)
                                commandsToExecute.add("inv")
                                commandsToExecute.removeAt(0)
                            } else {
                                opposite(currentRoute.removeAt(currentRoute.size - 1))
                            }
                        } else {
                            val direction = toVisit.first()
                            room.visitedDirections.add(direction)
                            currentRoute.add(direction)
                            direction
                        }
                    }
                } else if (stage == 1) {
                    val currentItems = input.filter { it.startsWith("-") }.map { it.replace("- ", "") }
                    itemVariations = currentItems.combinations().iterator()
                    stage = 2
                    "inv"
                } else if (stage == 2) {
                    val testDirection =
                        opposite(rooms.first { it.name == "Pressure-Sensitive Floor" }.directions.first())

                    val currentItems = input.filter { it.startsWith("-") }.map { it.replace("- ", "") }
                    val itemsToTry = itemVariations.next()
                    val toDrop = currentItems - itemsToTry
                    val toPickUp = itemsToTry - currentItems
                    commandsToExecute.addAll(toDrop.map { "drop $it" })
                    commandsToExecute.addAll(toPickUp.map { "take $it" })
                    commandsToExecute.add(testDirection)
                    stage = 3
                    commandsToExecute.removeAt(0)
                } else {
                    Thread.sleep(10L)
                    if (input.any { it.contains("ejected back to the checkpoint") }) {
                        stage = 2
                        "inv"
                    } else {
                        ""
                    }
                }
            }
        }
        log { answer }
        return answer.filter { it.isDigit() }
    }

    override fun b() = ""
}

data class Room(val name: String, val directions: List<String>, val items: List<String>, val route: List<String>) {
    val visitedDirections = mutableSetOf<String>()
}

fun asciiComputer(program: List<Long>, executor: (List<String>) -> String) =
    runBlocking {
        val receive = Channel<Long>(Channel.UNLIMITED)
        val buffer = mutableListOf<Char>()
        val comp = IntComp(Memory(program), receive) {
            yield()
            if (buffer.isNotEmpty()) {
                val c = buffer.removeAt(0)
                c.toLong()
            } else {
                val lines = generateSequence { receive.poll() }.map { it.toChar() }.joinToString("").cleanLines
                val result = executor(lines)
                if (result.isEmpty()) {
                    this.coroutineContext.cancel()
                    -1L
                } else {
                    buffer.addAll(result.toList())
                    buffer.add('\n')
                    buffer.removeAt(0).toLong()
                }
            }
        }
        comp.runToHalt()
        buildString {
            for (cl in receive) {
                append(cl.toChar())
            }
        }
    }
