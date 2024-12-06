package days

import adventOfCode.InputHandler
import adventOfCode.Solution
import adventOfCode.util.PairOf
import adventOfCode.util.ints
import kotlin.math.abs
import kotlin.math.sign

object Solution02 : Solution<List<List<Int>>>(AOC_YEAR, 2) {
    override fun getInput(handler: InputHandler) = handler.getInput("\n") { it.ints() }

    private enum class ReportSafety {
        UNSAFE, PARTIALLY_SAFE, SAFE
    }

    private fun getExpectedSign(differences: List<Int>): Int {
        val signFrequency = differences.groupingBy { it.sign }.eachCount()
        return if (signFrequency.getOrDefault(1, 0) > signFrequency.getOrDefault(-1, 0)) 1 else -1
    }

    private fun getSafety(report: List<Int>): ReportSafety {
        val differences = report.zipWithNext { a, b -> b - a }
        val expectedSign = getExpectedSign(differences)
        fun Int.isSafe() = abs(this) in 1..3 && this.sign == expectedSign

        val candidates = differences.mapIndexedNotNull { i, diff -> if (!diff.isSafe()) i else null }
        when (candidates.count()) {
            0 -> return ReportSafety.SAFE
            1 -> {
                val i = candidates.single()
                if (i == 0
                    || i == differences.lastIndex
                    || (differences[i] + differences[i - 1]).isSafe()
                    || (differences[i] + differences[i + 1]).isSafe()) {
                    return ReportSafety.PARTIALLY_SAFE
                }
            }
            2 -> {
                val i = candidates.first()
                val j = candidates.last()
                if (j == i + 1 && (differences[i] + differences[j]).isSafe()) {
                    return ReportSafety.PARTIALLY_SAFE
                }
            }
        }
        return ReportSafety.UNSAFE
    }

    override fun solve(input: List<List<Int>>): PairOf<Int> {
        return input.fold(0 to 0) { acc, report ->
            when (getSafety(report)) {
                ReportSafety.UNSAFE -> acc
                ReportSafety.PARTIALLY_SAFE -> acc.first to acc.second + 1
                ReportSafety.SAFE -> acc.first + 1 to acc.second + 1
            }
        }
    }
}
