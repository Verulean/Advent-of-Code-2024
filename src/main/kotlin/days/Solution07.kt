package days

import adventOfCode.InputHandler
import adventOfCode.Solution
import adventOfCode.util.PairOf
import adventOfCode.util.longs

object Solution07 : Solution<Collection<Pair<Long, List<Long>>>>(AOC_YEAR, 7) {
    override fun getInput(handler: InputHandler) = handler.getInput("\n") {
        val numbers = it.longs()
        numbers.first() to numbers.drop(1)
    }

    private fun Long.endsWith(other: Long) = this.toString().endsWith(other.toString())
    private fun Long.without(other: Long) = this.toString().substringBeforeLast(other.toString()).toLongOrNull() ?: 0

    private fun isTractable(testValue: Long, numbers: List<Long>, i: Int, allowConcat: Boolean): Boolean {
        val num = numbers[i]
        return when {
            i == 0 -> num == testValue
            testValue % num == 0L && isTractable(testValue / num, numbers, i - 1, allowConcat) -> true
            allowConcat && testValue.endsWith(num) && isTractable(testValue.without(num), numbers, i - 1, true) -> true
            else -> testValue > num && isTractable(testValue - num, numbers, i - 1, allowConcat)
        }
    }

    override fun solve(input: Collection<Pair<Long, List<Long>>>): PairOf<Long> {
        return input.fold(0L to 0L) { (sum1, sum2), (testValue, numbers) ->
            when {
                isTractable(testValue, numbers, numbers.lastIndex, false) -> sum1 + testValue to sum2 + testValue
                isTractable(testValue, numbers, numbers.lastIndex, true) -> sum1 to sum2 + testValue
                else -> sum1 to sum2
            }
        }
    }
}
