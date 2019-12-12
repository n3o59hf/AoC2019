package lv.n3o.aoc2019.tasks.t12

import lv.n3o.aoc2019.tasks.IO

private val data12 = """
    <x=-6, y=-5, z=-8>
    <x=0, y=-3, z=-13>
    <x=-15, y=10, z=-11>
    <x=-3, y=-8, z=3>
""".trimIndent()
private val answer12a = "5937"
private val answer12b = "376203951569712"

class IO : IO() {
    override val input = data12
    override val testA = answer12a
    override val testB = answer12b
}