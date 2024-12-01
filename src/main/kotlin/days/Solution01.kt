package days

import adventOfCode.InputHandler
import adventOfCode.Solution
import adventOfCode.util.PairOf
import adventOfCode.util.ints
import kotlin.math.abs

object Solution01 : Solution<PairOf<List<Int>>>(AOC_YEAR, 1) {
    override fun getInput(handler: InputHandler) = handler.getInput(delimiter = "\n")
        .map { it.ints().zipWithNext().single() }
        .unzip()

    override fun solve(input: PairOf<List<Int>>): PairOf<Int> {
        val lefts = input.first.sorted()
        val rights = input.second.sorted()
        val occurrences = rights.groupingBy { it }.eachCount()
        val ans1 = lefts.zip(rights).sumOf { abs(it.first - it.second) }
        val ans2 = lefts.sumOf { it * occurrences.getOrDefault(it, 0) }
        return ans1 to ans2
    }
}
