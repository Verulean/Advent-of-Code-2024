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

    private fun isTractable(testValue: Long, numbers: Collection<Long>, allowConcat: Boolean): Boolean {
        val num = numbers.first()
        val tail = numbers.drop(1)
        return when {
            tail.isEmpty() -> num == testValue
            testValue < 0 -> false
            testValue % num == 0L && isTractable(testValue / num, tail, allowConcat) -> true
            allowConcat && testValue.endsWith(num) && isTractable(testValue.without(num), tail, true) -> true
            else -> isTractable(testValue - num, tail, allowConcat)
        }
    }

    override fun solve(input: Collection<Pair<Long, List<Long>>>): PairOf<Long> {
        return input.fold(0L to 0L) { (sum1, sum2), (testValue, numbers) ->
            when {
                isTractable(testValue, numbers.reversed(), false) -> sum1 + testValue to sum2 + testValue
                isTractable(testValue, numbers.reversed(), true) -> sum1 to sum2 + testValue
                else -> sum1 to sum2
            }
        }
    }
}
