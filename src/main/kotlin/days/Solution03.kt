package days

import adventOfCode.*
import adventOfCode.util.*

object Solution03 : Solution<String>(AOC_YEAR, 3) {
    override fun getInput(handler: InputHandler) = handler.getInput()

    override fun solve(input: String): PairOf<Int> {
        val pattern = Regex("""mul\((\d+),(\d+)\)|(do\(\))|(don't\(\))""")
        val (ans1, ans2, _) = pattern.findAll(input).fold(Triple(0, 0, true)) { (sum1, sum2, mulEnabled), match ->
            val (_, num1, num2, doCmd, dontCmd) = match.groupValues
            when {
                doCmd.isNotBlank() -> Triple(sum1, sum2, true)
                dontCmd.isNotBlank() -> Triple(sum1, sum2, false)
                else -> {
                    val product = num1.toInt() * num2.toInt()
                    Triple(sum1 + product, if (mulEnabled) sum2 + product else sum2, mulEnabled)
                }
            }
        }
        return ans1 to ans2
    }
}
