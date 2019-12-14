package lv.n3o.aoc2019.tasks.t14

import lv.n3o.aoc2019.tasks.Task
import lv.n3o.aoc2019.tasks.cleanLines
import lv.n3o.aoc2019.tasks.divRoundedUp

@Suppress("unused")
class Task : Task() {
    private val data by lazy {
        input.cleanLines.map { line ->
            val splitLine = line
                .replace(", ", ":")
                .replace(" => ", ":")
                .split(":")
            val formula =
                Formula(
                    AmountHolder.parse(splitLine.last()),
                    splitLine.dropLast(1).map(AmountHolder.Companion::parse)
                )
            formula.result.chem to formula
        }.toMap()
    }

    private val orePerOneFuel by lazy {
        val chemReqs = mutableMapOf("FUEL" to 1L)

        processNeeds(chemReqs)

        chemReqs["ORE"] ?: 0L
    }

    private fun processNeeds(chemReqs: MutableMap<String, Long>) {
        val needs = generateSequence { (chemReqs.filter { it.value > 0L }.keys - "ORE").firstOrNull() }
        for (material in needs) {
            val formula = data[material] ?: error("Unknown material")

            val requiredReactions = (chemReqs[material] ?: 0L).divRoundedUp(formula.result.amount)

            chemReqs[material] = (chemReqs[material] ?: 0L) - formula.result.amount * requiredReactions
            formula.ingredients.forEach {
                chemReqs[it.chem] = (chemReqs[it.chem] ?: 0L) + it.amount * requiredReactions
            }
        }
    }

    override fun a() = "$orePerOneFuel"

    override fun b(): String {
        val chemReqs = mutableMapOf("ORE" to -1000000000000L)
        fun ore() = (chemReqs["ORE"] ?: 0L)
        var maxFuel = 0L
        while (ore() < 0L) {
            val additionalFuel = (0 - (ore() / orePerOneFuel)).coerceAtLeast(1L)
            chemReqs["FUEL"] = additionalFuel

            processNeeds(chemReqs)

            if ((chemReqs["ORE"] ?: 0L) < 0L) {
                maxFuel += additionalFuel
            }
        }
        return "$maxFuel"
    }
}

data class Formula(val result: AmountHolder, val ingredients: List<AmountHolder>)

data class AmountHolder(val chem: String, val amount: Long) {
    companion object {
        fun parse(input: String): AmountHolder {
            val (amount, chem) = input.split(" ")
            return AmountHolder(chem, amount.toLong())
        }
    }
}