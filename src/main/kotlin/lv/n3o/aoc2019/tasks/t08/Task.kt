package lv.n3o.aoc2019.tasks.t08

import lv.n3o.aoc2019.tasks.Task

@Suppress("unused")
class Task : Task() {
    val imageLayers by lazy {
        val chunkSize = data08width * data08height
        data08
            .windowed(chunkSize, chunkSize)
    }

    val imagePixels by lazy {
        List(data08width * data08height) { i ->
            imageLayers.asSequence().map { it[i] }.first { it != '2' }
        }.windowed(data08width, data08width)
    }

    override fun a(): String {
        return imageLayers.minBy { layer ->
            layer.count { it == '0' }
        }?.let { layer ->
            layer.count { it == '1' } * layer.count { it == '2' }
        }.toString()
    }

    override fun b(): String {
        // Simple approach
        imagePixels.forEach { line ->
            line.map {
                when (it) {
                    '0' -> ' '
                    else -> '█'
                }
            }.let { log(it.joinToString("")) }
        }

        // Decoding to text
        return imagePixels
            .asSequence()
            .map { it.windowed(4, 5) }
            .reduce { acc, next -> acc.zip(next).map { it.first + it.second } }
            .map { c ->
                (0..3)
                    .map {
                        c
                            .drop(it)
                            .filterIndexed { index, _ -> index % 4 == 0 }
                            .foldRight(0) { px, acc ->
                                acc.shl(1) + if (px == '0') 0 else 1
                            }
                    }
                    .fold(0L) { acc, l ->
                        acc.shl(8) + l
                    }
            }
            .map { fontColumns[it] }
            .joinToString("")
    }
}