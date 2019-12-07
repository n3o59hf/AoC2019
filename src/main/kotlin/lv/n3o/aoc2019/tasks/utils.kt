package lv.n3o.aoc2019.tasks

val String.cleanLines get() = lines().map { it.trim() }.filter { it.isNotBlank() }

fun <T> List<T>.infinite() = sequence {
    while (true) {
        yieldAll(this@infinite)
    }
}

fun <T> List<T>.permute(): List<List<T>> {
    if (size == 1) return listOf(this)

    val permutations = mutableListOf<List<T>>()
    val movableElement = first()
    for (p in drop(1).permute())
        for (i in 0..p.size) {
            val mutation = p.toMutableList()
            mutation.add(i, movableElement)
            permutations.add(mutation)
        }
    return permutations
}