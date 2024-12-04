package days

import adventOfCode.InputHandler
import adventOfCode.Solution
import adventOfCode.util.PairOf

object Solution04 : Solution<List<String>>(AOC_YEAR, 4) {
    override fun getInput(handler: InputHandler) = handler.getInput("\n")

    private val xmas = setOf("XMAS", "SAMX")

    override fun solve(input: List<String>): PairOf<Int> {
        var ans1 = 0
        var ans2 = 0
        val m = input.size
        val n = input.first().count()
        input.withIndex().forEach { (i, row) ->
            row.withIndex().forEach { (j, c) ->
                // Column
                if (i <= m - 4 && (0..3).map { input[i + it][j] }.joinToString("") in xmas) {
                    ans1 += 1
                }
                if (j <= n - 4) {
                    // Row
                    if (row.substring(j, j + 4) in xmas) {
                        ans1 += 1
                    }
                    // Main diagonal
                    if (i <= m - 4 && (0..3).map { input[i + it][j + it] }.joinToString("") in xmas) {
                        ans1 += 1
                    }
                    // Off diagonal
                    if (i >= 3 && (0..3).map { input[i - it][j + it] }.joinToString("") in xmas) {
                        ans1 += 1
                    }
                }
                // Part 2
                if (i in 1..m - 2 && j in 1..n - 2 && c == 'A') {
                    if (setOf(input[i - 1][j - 1], input[i + 1][j + 1]) == setOf('M', 'S')
                        && setOf(input[i - 1][j + 1], input[i + 1][j - 1]) == setOf('M', 'S')) {
                        ans2 += 1
                    }
                }
            }
        }
        return ans1 to ans2
    }
}
