package lv.n3o.aoc2019.tasks

import lv.n3o.aoc2019.formatTime
import lv.n3o.aoc2019.timeFromApplicationStart

typealias DebugListener = (String) -> Unit

abstract class Task {
    lateinit var input: String
    var debugListener: DebugListener? = null

    fun isLoggerOn() = debugListener != null
    fun log(vararg things: Any?) {
        debugListener ?: return

        val logline = things.map { it ?: "<null>" }.joinToString(" ") { it.toString() }
        val className = this::class.java.canonicalName.split('.').takeLast(2)[0].padEnd(3, ' ')
        val time = timeFromApplicationStart.formatTime()
        val methodName = Thread.currentThread().stackTrace[2].methodName

        debugListener?.let { it(("$time ($className.$methodName): $logline\n")) }
    }

    open fun a(): String = ""
    open fun b(): String = ""
}