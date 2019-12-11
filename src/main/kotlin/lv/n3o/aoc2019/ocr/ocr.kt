package lv.n3o.aoc2019.ocr

fun List<List<Char>>.recognizeLetters() = asSequence()
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
    .map { fontColumns[it] ?: "�" }
    .joinToString("")