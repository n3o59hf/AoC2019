package lv.n3o.aoc2019.tasks.t24

import lv.n3o.aoc2019.tasks.IO

private val data24 = """
    ##...
    #.###
    .#.#.
    #....
    ..###
""".trimIndent()
private val answer24a = "18350099"
private val answer24b = "2037"

class IO : IO() {
    override val input = data24
    override val testA = answer24a
    override val testB = answer24b
}