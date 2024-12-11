package days

import adventOfCode.InputHandler
import adventOfCode.Solution
import adventOfCode.util.PairOf

private typealias StoneCounter = Map<String, ULong>

object Solution11 : Solution<StoneCounter>(AOC_YEAR, 11) {
    override fun getInput(handler: InputHandler) = handler.getInput(" ")
        .groupingBy { it }
        .eachCount()
        .mapValues { it.value.toULong() }

    private fun String.blink() = when {
        this == "0" -> sequenceOf("1")
        this.length % 2 == 0 -> sequenceOf(this.substring(0, this.length / 2), this.substring(this.length / 2))
        else -> sequenceOf((this.toULong() * 2024UL).toString())
    }

    private fun StoneCounter.blink(): StoneCounter {
        val newStones = mutableMapOf<String, ULong>().withDefault { 0UL }
        this.entries.forEach { (stone, count) ->
            stone.blink().forEach { newStones[it] = newStones.getValue(it) + count }
        }
        return newStones
    }

    override fun solve(input: StoneCounter): PairOf<ULong> {
        var stones = input
        repeat(25) { stones = stones.blink() }
        val ans1 = stones.values.sum()
        repeat(50) { stones = stones.blink() }
        return ans1 to stones.values.sum()
    }
}
