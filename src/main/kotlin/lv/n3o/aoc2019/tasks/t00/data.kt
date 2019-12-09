package lv.n3o.aoc2019.tasks.t00

import lv.n3o.aoc2019.tasks.IO

private val data00 = "Hello\nworld!"
private val answer00a = "Hello"
private val answer00b = "world!"

class IO : IO() {
    override val input = data00
    override val testA = answer00a
    override val testB = answer00b
}