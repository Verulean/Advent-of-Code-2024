package days

import adventOfCode.InputHandler
import adventOfCode.Solution
import adventOfCode.util.PairOf
import adventOfCode.util.ints

private typealias OrderingRule = PairOf<Int>
private typealias PrinterUpdate = Map<Int, Int>

object Solution05 : Solution<Pair<Collection<OrderingRule>, Collection<PrinterUpdate>>>(AOC_YEAR, 5) {
    override fun getInput(handler: InputHandler): Pair<Collection<OrderingRule>, Collection<PrinterUpdate>> {
        val (first, second) = handler.getInput("\n\n")
        return Pair(
            first.split("\n").map { it.ints().zipWithNext().single() },
            second.split("\n").map { it.ints().withIndex().associate { (i, n) -> n to i } }
        )
    }

    private fun OrderingRule.appliesTo(update: PrinterUpdate) = update.containsKey(this.first) && update.containsKey(this.second)
    private fun OrderingRule.isSatisfied(update: PrinterUpdate) = update.getValue(this.first) < update.getValue(this.second)
    private val PrinterUpdate.middlePage get() = this.entries.first { it.value == this.size / 2 }.key
    private fun PrinterUpdate.isSorted(rules: Collection<OrderingRule>) = rules.all { !it.appliesTo(this) || it.isSatisfied(this) }

    private fun rectifyUpdate(rules: Collection<OrderingRule>, update: PrinterUpdate): Int {
        val newUpdate = update.toMutableMap()
        val applicableRules = rules.filter { it.appliesTo(update) }
        while (true) {
            var changed = false
            applicableRules.forEach {
                if (!it.isSatisfied(newUpdate)) {
                    val (a, b) = it
                    val temp = newUpdate.getValue(a)
                    newUpdate[a] = newUpdate.getValue(b)
                    newUpdate[b] = temp
                    changed = true
                    return@forEach
                }
            }
            if (!changed) {
                return newUpdate.middlePage
            }
        }
    }

    override fun solve(input: Pair<Collection<OrderingRule>, Collection<PrinterUpdate>>): PairOf<Int> {
        val (rules, updates) = input
        return updates.fold(0 to 0) { acc, update ->
            if (update.isSorted(rules)) acc.first + update.middlePage to acc.second
            else acc.first to acc.second + rectifyUpdate(rules, update)
        }
    }
}
