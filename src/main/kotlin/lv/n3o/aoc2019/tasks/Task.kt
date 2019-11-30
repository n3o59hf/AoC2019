package lv.n3o.aoc2019.tasks

typealias DebugListener = (String) -> Unit

abstract class Task {
    var debugListener: DebugListener? = null

    fun log(vararg things: Any?) {
        debugListener ?: return

        val logline = things.map { it ?: "<null>" }.joinToString(" ") { it.toString() }
        val className = this::class.java.canonicalName.split('.').takeLast(2)[0].padEnd(3, ' ')
        val time = timeFromApplicationStart.formatTime()
        val methodName = Thread.currentThread().stackTrace[2].methodName

        println("$time ($className.$methodName): $logline")
    }

    open fun a(): String = ""
    open fun b(): String = ""
}