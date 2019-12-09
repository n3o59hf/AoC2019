package lv.n3o.aoc2019.tasks

abstract class IO {
    abstract val input: String
    open val testA: String? = null
    open val testB: String? = null
}