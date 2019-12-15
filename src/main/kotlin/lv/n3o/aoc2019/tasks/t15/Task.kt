package lv.n3o.aoc2019.tasks.t15

import kotlinx.coroutines.runBlocking
import lv.n3o.aoc2019.coords.C
import lv.n3o.aoc2019.coords.Coord2d
import lv.n3o.aoc2019.coords.minus
import lv.n3o.aoc2019.tasks.Task
import lv.n3o.aoc2019.tasks.doStepComputation
import kotlin.properties.Delegates

@Suppress("unused")
class Task : Task() {
    private val programNumbers by lazy { input.split(",").map(String::toLong) }


    val data by lazy {
        var goal: Coord2d? = null
        val field = mutableMapOf<Coord2d, Int>()

        var depth = 0
        var position: Coord2d = C(0, 0)
        field[position] = depth

        runBlocking {
            doStepComputation(this, programNumbers) {
                var visitNeighbors: suspend () -> Unit by Delegates.notNull()

                suspend fun goDeeper(direction: Direction) {

                    val response = doStep(direction.id)
                    if (response == 0L) {
                        field[position + direction.vector] = -1
                        return
                    }

                    depth++
                    position += direction.vector

                    field[position] = depth
                    if (response == 2L) {
                        goal = position
                    }
                    visitNeighbors()

                    if (doStep(direction.oppositeId) == 0L) error("Inconsistency in wall positioning")

                    position -= direction.vector
                    depth--
                }

                visitNeighbors = {
                    Direction.ALL.forEach { direction ->
                        if (field[position + direction.vector] ?: Int.MAX_VALUE > depth) {
                            goDeeper(direction)
                        }
                    }
                }
                visitNeighbors()
            }
        }

        field.toMap() to goal
    }

    override fun a() = "${data.first[data.second]}"

    override fun b(): String {
        val field = data.first
        val oxyField = mutableMapOf<Coord2d, Int>()
        var depth = 0
        var position: Coord2d = data.second ?: error("a() should be launched first")
        oxyField[position] = depth

        var visitNeighbors: () -> Unit by Delegates.notNull()

        fun goDeeper(direction: Direction) {
            if (field[position + direction.vector] == -1) {
                oxyField[position + direction.vector] = -1
                return
            } else {
                depth++
                position += direction.vector
                oxyField[position] = depth
                visitNeighbors()
                position -= direction.vector
                depth--
            }
        }
        visitNeighbors = {
            Direction.ALL.forEach {
                val cell = oxyField[position + it.vector]
                if (cell == null || cell > depth) {
                    goDeeper(it)
                }
            }
        }

        visitNeighbors()

        return "${oxyField.values.max()}"
    }
}

@Suppress("unused")
private enum class Direction(val id: Long, val oppositeId: Long, val vector: C) {
    NORTH(1, 2, C(0, -1)),
    SOUTH(2, 1, C(0, 1)),
    WEST(3, 4, C(-1, 0)),
    EAST(4, 3, C(1, 0));

    companion object {
        val ALL = values()
    }
}
