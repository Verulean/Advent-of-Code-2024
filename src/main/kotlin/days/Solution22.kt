package days

import adventOfCode.InputHandler
import adventOfCode.Solution
import adventOfCode.util.longs

object Solution22 : Solution<List<Long>>(AOC_YEAR, 22) {
    override fun getInput(handler: InputHandler) = handler.getInput().longs()

    private const val PRUNE_MODULO = 16777216L
    private const val TRIGGER_LENGTH = 4

    private fun Long.evolve(): Long {
        var n = this
        n = n xor (n shl 6) % PRUNE_MODULO
        n = n xor (n shr 5) % PRUNE_MODULO
        n = n xor (n shl 11) % PRUNE_MODULO
        return n
    }
    private val Long.price get() = if (this >= 0) this % 10 else -(-this % 10)

    override fun solve(input: List<Long>): Pair<Any?, Any?> {
        var ans1 = 0L
        val profits: MutableMap<List<Long>, Long> = mutableMapOf()
        input.forEach { secretNumber ->
            val numbers = generateSequence(secretNumber) { it.evolve() }.take(2001).toList()
            ans1 += numbers.last()
            val prices = numbers.map { it.price }
            val deltas = prices.zipWithNext { a, b -> b - a }
            val seenTriggers: MutableSet<List<Long>> = mutableSetOf()
            deltas.windowed(TRIGGER_LENGTH).zip(prices.drop(TRIGGER_LENGTH)).forEach { (trigger, price) ->
                if (seenTriggers.add(trigger)) profits.merge(trigger, price, Long::plus)
            }
        }
        return ans1 to profits.values.max()
    }
}
