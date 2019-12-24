package lv.n3o.aoc2019.tasks.t24

import lv.n3o.aoc2019.coords.C2
import lv.n3o.aoc2019.tasks.Task
import lv.n3o.aoc2019.tasks.cleanLines

@Suppress("unused")
class Task : Task() {
    val data by lazy { input.cleanLines }

    override fun a(): String {
        var grid = BugGrid(data)

        val seen = mutableSetOf(grid.rating)

        do {
            grid = grid.evolve()
        } while (seen.add(grid.rating))

        return grid.rating.toString()
    }

    override fun b(): String {
        val empty = BugGridND()
        var nd = listOf(BugGridND(data))

        for (i in 0 until 200) {
            val newNd = listOf(empty, empty) + nd + listOf(empty, empty)
            nd = newNd.windowed(3, 1).map { (parent, current, child) ->
                current.evolve(parent, child)
            }
        }

        return nd.sumBy { it.bugCount }.toString()
    }
}

inline class BugGrid(val rating: Int = 0) {
    constructor(lines: List<String>) : this(
        lines
            .mapIndexed { y, chars ->
                chars.mapIndexed { x, c ->
                    if (c == '#') C2(x, y) else null
                }
            }
            .flatten()
            .filterNotNull()
            .fold(BugGrid()) { grid, bug ->
                grid.mutate(bug.x, bug.y, true)
            }
            .rating
    )

    private fun index(x: Int, y: Int) = x + y * 5
    operator fun get(x: Int, y: Int): Boolean {
        if (x < 0 || x > 4 || y < 0 || y > 4) return false
        return rating and (1 shl index(x, y)) > 0
    }

    fun nextState(x: Int, y: Int): Boolean {
        var counter = 0
        if (this[x - 1, y]) counter++
        if (this[x + 1, y]) counter++
        if (this[x, y - 1]) counter++
        if (this[x, y + 1]) counter++

        return if (this[x, y]) {
            counter == 1
        } else {
            counter in 1..2
        }
    }

    fun mutate(x: Int, y: Int, value: Boolean) = when {
        value -> BugGrid(rating or (1 shl index(x, y)))
        this[x, y] -> BugGrid(rating xor (1 shl index(x, y)))
        else -> this
    }

    fun evolve(): BugGrid {
        var newGrid = BugGrid()
        for (y in 0..4) {
            for (x in 0..4) {
                if (nextState(x, y)) {
                    newGrid = newGrid.mutate(x, y, true)
                }
            }
        }

        return newGrid
    }

    override fun toString(): String = buildString {
        append('\n')
        for (y in 0..4) {
            for (x in 0..4) {
                append(if (this@BugGrid[x, y]) '#' else '.')
            }
            append('\n')
        }
    }
}

inline class BugGridND(val rating: Int = 0) {
    constructor(lines: List<String>) : this(
        lines
            .mapIndexed { y, chars ->
                chars.mapIndexed { x, c ->
                    if (c == '#') C2(x, y) else null
                }
            }
            .flatten()
            .filterNotNull()
            .fold(BugGrid()) { grid, bug ->
                grid.mutate(bug.x, bug.y, true)
            }
            .rating
    )

    val bugCount
        get() = (0..4).sumBy { y ->
            (0..4).count { x ->
                if (x == 2 && y == 2) false
                else get(x, y, x, y, null, null) > 0
            }
        }

    private fun index(x: Int, y: Int) = x + y * 5

    fun get(x: Int, y: Int, fromX: Int, fromY: Int, parent: BugGridND?, child: BugGridND?): Int {
        if (x < 0) return parent?.get(1, 2, x, y, null, this) ?: error("Parent not supplied")
        if (x > 4) return parent?.get(3, 2, x, y, null, this) ?: error("Parent not supplied")
        if (y < 0) return parent?.get(2, 1, x, y, null, this) ?: error("Parent not supplied")
        if (y > 4) return parent?.get(2, 3, x, y, null, this) ?: error("Parent not supplied")

        if (x == 2 && y == 2) {
            child ?: error("Child not supplied")
            if (fromX < 2) return 0 +
                    child.get(0, 0, x, y, this, null) +
                    child.get(0, 1, x, y, this, null) +
                    child.get(0, 2, x, y, this, null) +
                    child.get(0, 3, x, y, this, null) +
                    child.get(0, 4, x, y, this, null)
            if (fromX > 2) return 0 +
                    child.get(4, 0, x, y, this, null) +
                    child.get(4, 1, x, y, this, null) +
                    child.get(4, 2, x, y, this, null) +
                    child.get(4, 3, x, y, this, null) +
                    child.get(4, 4, x, y, this, null)
            if (fromY < 2) return 0 +
                    child.get(0, 0, x, y, this, null) +
                    child.get(1, 0, x, y, this, null) +
                    child.get(2, 0, x, y, this, null) +
                    child.get(3, 0, x, y, this, null) +
                    child.get(4, 0, x, y, this, null)
            if (fromY > 2) return 0 +
                    child.get(0, 4, x, y, this, null) +
                    child.get(1, 4, x, y, this, null) +
                    child.get(2, 4, x, y, this, null) +
                    child.get(3, 4, x, y, this, null) +
                    child.get(4, 4, x, y, this, null)
        }

        return if (rating and (1 shl index(x, y)) > 0) 1 else 0
    }

    fun nextState(x: Int, y: Int, parent: BugGridND, child: BugGridND): Boolean {
        val counter = 0 +
                this.get(x - 1, y, x, y, parent, child) +
                this.get(x + 1, y, x, y, parent, child) +
                this.get(x, y - 1, x, y, parent, child) +
                this.get(x, y + 1, x, y, parent, child)

        return if (this.get(x, y, x, y, null, null) > 0) {
            counter == 1
        } else {
            counter in 1..2
        }
    }

    fun mutate(x: Int, y: Int, value: Boolean) = when {
        value -> BugGridND(rating or (1 shl index(x, y)))
        get(x, y, x, y, null, null) > 0 -> BugGridND(rating xor (1 shl index(x, y)))
        else -> this
    }

    fun evolve(parent: BugGridND, child: BugGridND): BugGridND {
        var newGrid = BugGridND()
        for (y in 0..4) {
            for (x in 0..4) {
                if (!(x == 2 && y == 2) && nextState(x, y, parent, child)) {
                    newGrid = newGrid.mutate(x, y, true)
                }
            }
        }

        return newGrid
    }

    override fun toString(): String = buildString {
        append('\n')
        for (y in 0..4) {
            for (x in 0..4) {
                if (x == 2 && y == 2) append('?') else
                    append(if (this@BugGridND.get(x, y, x, y, null, null) > 0) '#' else '.')
            }
            append('\n')
        }
    }
}
