package days

import adventOfCode.InputHandler
import adventOfCode.Solution
import adventOfCode.util.PairOf

object Solution04 : Solution<List<String>>(AOC_YEAR, 4) {
    override fun getInput(handler: InputHandler) = handler.getInput("\n")

    private val xmas = "XMAS"
    private val directions = arrayOf(-1 to -1, -1 to 0, -1 to 1, 0 to 1, 1 to 1, 1 to 0, 1 to -1, 0 to -1)

    override fun solve(input: List<String>): PairOf<Int> {
        fun getLetter(i: Int, j: Int) = input.getOrNull(i)?.getOrNull(j)
        var ans1 = 0
        var ans2 = 0
        input.withIndex().forEach { (i, row) ->
            row.withIndex().forEach { (j, c) ->
                if (c == xmas.first()) {
                    directions.forEach { (di, dj) ->
                        if ((1..3).all { getLetter(i + di * it, j + dj * it) == xmas[it] }) {
                            ans1 += 1
                        }
                    }
                }
                if (c == 'A') {
                    if (setOf(getLetter(i - 1, j - 1), getLetter(i + 1, j + 1)) == setOf('M', 'S')
                        && setOf(getLetter(i - 1, j + 1), getLetter(i + 1, j - 1)) == setOf('M', 'S')) {
                        ans2 += 1
                    }
                }
            }
        }
        return ans1 to ans2
    }
}
