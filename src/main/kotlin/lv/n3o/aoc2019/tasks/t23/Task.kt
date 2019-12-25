package lv.n3o.aoc2019.tasks.t23

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import lv.n3o.aoc2019.tasks.IntComp
import lv.n3o.aoc2019.tasks.Memory
import lv.n3o.aoc2019.tasks.Task
import java.util.*
import kotlin.coroutines.CoroutineContext

@Suppress("unused")
class Task : Task() {
    private val programNumbers by lazy { input.split(",").map(String::toLong) }

    override fun a(): String {
        val y = runBlocking {
            val job = Job()
            val context = coroutineContext + job
            val network = Network(programNumbers, context)
            for (i in 0 until 50) {
                network.initializeComputer(i.toLong())
            }

            val output = network.initializeOutput(255L)

            network.start()

            val (_, y) = output.receive()
            job.cancelAndJoin()
            y
        }

        return y.toString()
    }

    override fun b(): String {
        val y = runBlocking {
            val job = Job()
            val context = coroutineContext + job
            val network = Network(programNumbers, context)
            for (i in 0 until 50) {
                network.initializeComputer(i.toLong())
            }

            val output = network.initializeOutput(255L)

            network.start()

            var currentPacket: Pair<Long, Long>? = null
            var sentY: Long? = null
            while (true) {
                val packet = output.poll()
                if (packet != null) {
                    currentPacket = packet
                }

                if (packet == null && network.systemIdle.isNotEmpty() && network.systemIdle.all { it.value } && currentPacket != null) {
                    if (currentPacket.second == sentY) {
                        job.cancelAndJoin()
                        return@runBlocking currentPacket.second
                    }
                    sentY = currentPacket.second
                    network.send(0L, currentPacket.first, currentPacket.second)
                    currentPacket = null
                }
                yield()
            }
        }

        return y.toString()
    }
}

class Network(val program: List<Long>, override val coroutineContext: CoroutineContext) : CoroutineScope {
    val computers = mutableMapOf<Long, Pair<Channel<Pair<Long, Long>>, IntComp?>>()
    var systemIdle = Collections.synchronizedMap(mutableMapOf<Long,Boolean>())
    fun initializeComputer(computerAddress: Long) {
        val inputBuffer = Channel<Pair<Long, Long>>(Channel.UNLIMITED)
        val outputBuffer = Channel<Long>(Channel.UNLIMITED)
        val outputData = Channel<Triple<Long, Long, Long>>()
        var input: Pair<Long, Long>? = null
        var initialized = false
        val comp = IntComp(Memory(program), outputBuffer) {
            yield()
            if (!initialized) {
                initialized = true
                return@IntComp computerAddress
            }

            if (input == null) {
                input = inputBuffer.poll()
                if (input == null) {
                    systemIdle[computerAddress] = true
                    -1
                } else {
                    systemIdle[computerAddress] = false
                    input!!.first
                }
            } else {
                val y = input!!.second
                input = null
                y
            }
        }

        launch {
            var address: Long? = null
            var x: Long? = null

            for (i in outputBuffer) {
                when {
                    address == null -> address = i
                    x == null -> x = i
                    else -> {
                        outputData.send(Triple(address, x, i))
                        address = null
                        x = null
                    }
                }
            }
        }

        launch {
            for (p in outputData) {
                val (address, x, y) = p
                computers[address]?.first?.send(x to y)
            }
        }

        launch {
            inputBuffer.send(-1L to -1L)
        }

        computers[computerAddress] = inputBuffer to comp
    }

    fun initializeOutput(address: Long): ReceiveChannel<Pair<Long, Long>> {
        val channel = Channel<Pair<Long, Long>>()
        computers[address] = channel to null
        return channel
    }

    fun start() {
        computers.values.mapNotNull { it.second }.forEach {
            launch {
                it.runToHalt()
            }
        }
    }

    suspend fun send(address: Long, x: Long, y: Long) {
        computers[address]?.first?.send(x to y)
    }
}
