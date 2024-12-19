package days

import adventOfCode.InputHandler
import adventOfCode.Solution
import adventOfCode.util.PairOf
import adventOfCode.util.longs

private typealias Program = List<Long>

object Solution17 : Solution<Pair<Long, Program>>(AOC_YEAR, 17) {
    override fun getInput(handler: InputHandler): Pair<Long, Program> {
        val (registerStr, programStr) = handler.getInput("\n\n")
        return registerStr.longs().first() to programStr.longs()
    }

    private fun Program.step(registerA: Long): PairOf<Long> {
        var (a, b, c) = Triple(registerA, 0L, 0L)
        fun combo(n: Long) = when (n) {
            in 0L..3L -> n
            4L -> a
            5L -> b
            6L -> c
            else -> error("Invalid operand")
        }
        var output = 0L
        this.chunked(2).forEach { (instruction, op) ->
            when (instruction) {
                0L -> a = a shr combo(op).toInt()
                1L -> b = b xor op
                2L -> b = combo(op) % 8L
                4L -> b = b xor c
                5L -> output = combo(op) % 8L
                6L -> b = a shr combo(op).toInt()
                7L -> c = a shr combo(op).toInt()
            }
        }
        return a to output
    }

    private fun Program.run(registerA: Long): Program {
        var a = registerA
        val output: MutableList<Long> = mutableListOf()
        while (a != 0L) {
            val (newA, out) = this.step(a)
            a = newA
            output.add(out)
        }
        return output
    }

    override fun solve(input: Pair<Long, Program>): Pair<String, Long?> {
        val (registerA, program) = input
        val ans1 = program.run(registerA).joinToString(",")
        var quineInputs = setOf(0L)
        program.reversed().forEach { n ->
            val newQuines: MutableSet<Long> = mutableSetOf()
            quineInputs.forEach { currA ->
                (0L..7L).forEach { newBlock ->
                    val newA = (currA shl 3) + newBlock
                    if (program.step(newA).second == n) newQuines.add(newA)
                }
            }
            quineInputs = newQuines
        }
        return ans1 to quineInputs.minOrNull()
    }
}
