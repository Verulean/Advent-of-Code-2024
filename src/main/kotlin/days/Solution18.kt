package days

import adventOfCode.InputHandler
import adventOfCode.Solution
import adventOfCode.util.Point2D
import adventOfCode.util.ints
import adventOfCode.util.plus

object Solution18 : Solution<List<Point2D>>(AOC_YEAR, 18) {
    override fun getInput(handler: InputHandler) = handler.getInput().ints().chunked(2).map { it.first() to it.last() }

    private fun findMinSteps(bytes: Set<Point2D>, goal: Point2D): Int? {
        val iBounds = 0..goal.first
        val jBounds = 0..goal.second
        val seen: MutableSet<Point2D> = mutableSetOf()
        val q = ArrayDeque<Pair<Int, Point2D>>()
        q.add(0 to (0 to 0))
        while (q.isNotEmpty()) {
            val (cost, pos) = q.removeFirst()
            if (pos == goal) return cost
            if (!seen.add(pos)) continue
            sequenceOf(-1 to 0, 0 to 1, 1 to 0, 0 to -1).map { pos + it }
                .filter { it !in bytes && it.first in iBounds && it.second in jBounds }
                .forEach { q.add(cost + 1 to it) }
        }
        return null
    }

    private fun findFirstBlocker(allBytes: List<Point2D>, goal: Point2D): Point2D {
        var lo = 0
        var hi = allBytes.size
        while (lo < hi - 1) {
            val mid = (lo + hi) / 2
            when (findMinSteps(allBytes.subList(0, mid).toSet(), goal)) {
                null -> hi = mid
                else -> lo = mid
            }
        }
        return allBytes[lo]
    }

    override fun solve(input: List<Point2D>): Pair<Int?, String> {
        val goal = 70 to 70
        val ans1 = findMinSteps(input.subList(0, 1024).toSet(), goal)
        val ans2 = findFirstBlocker(input, goal).let { "${it.first},${it.second}" }
        return ans1 to ans2
    }
}
