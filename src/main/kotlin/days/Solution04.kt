package days

import adventOfCode.InputHandler
import adventOfCode.Solution
import adventOfCode.util.PairOf

object Solution04 : Solution<List<String>>(AOC_YEAR, 4) {
    override fun getInput(handler: InputHandler) = handler.getInput("\n")

    private const val xmas = "XMAS"
    private val directions = arrayOf(-1 to -1, -1 to 0, -1 to 1, 0 to 1, 1 to 1, 1 to 0, 1 to -1, 0 to -1)
    private val ms = setOf('M', 'S')

    private operator fun List<String>.get(i: Int, j: Int) = this.getOrNull(i)?.getOrNull(j)

    override fun solve(input: List<String>): PairOf<Int> {
        fun getLetter(i: Int, j: Int) = input.getOrNull(i)?.getOrNull(j)
        var ans1 = 0
        var ans2 = 0
        input.withIndex().forEach { (i, row) ->
            row.withIndex().forEach { (j, c) ->
                when (c) {
                    xmas.first() -> {
                        ans1 += directions.count { (di, dj) ->
                            (1..<xmas.length).all { getLetter(i + di * it, j + dj * it) == xmas[it] }
                        }
                    }
                    'A' -> {
                        if (setOf(input[i - 1, j - 1], input[i + 1, j + 1]) == ms
                            && setOf(input[i - 1, j + 1], input[i + 1, j - 1]) == ms) {
                            ans2 += 1
                        }
                    }
                }
            }
        }
        return ans1 to ans2
    }
}
