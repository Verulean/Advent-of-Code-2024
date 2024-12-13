package days

import adventOfCode.InputHandler
import adventOfCode.Solution
import adventOfCode.util.longs

object Solution13 : Solution<Collection<List<Long>>>(AOC_YEAR, 13) {
    override fun getInput(handler: InputHandler) = handler.getInput("\n\n").map { it.longs() }

    private operator fun List<Long>.component6() = this[5]

    private fun getTokenCost(ax: Long, ay: Long, bx: Long, by: Long, x: Long, y: Long): Long {
        // Solve the system:
        // na * ax + nb * bx = x
        // na * ay + nb * by = y
        val lhs = ax * by - ay * bx
        val rhs = x * by - y * bx
        if (rhs % lhs != 0L) return 0L
        val na = rhs / lhs
        val rhs2 = x - na * ax
        if (rhs2 % bx != 0L) return 0L
        val nb = rhs2 / bx
        return 3 * na + nb
    }

    override fun solve(input: Collection<List<Long>>) = input.fold(0L to 0L) { (sum1, sum2), (ax, ay, bx, by, x, y) ->
        sum1 + getTokenCost(ax, ay, bx, by, x, y) to
            sum2 + getTokenCost(ax, ay, bx, by, x + 10_000_000_000_000L, y + 10_000_000_000_000L)
    }
}
