package days

import adventOfCode.InputHandler
import adventOfCode.Solution

object Solution19 : Solution<Pair<Set<String>, Collection<String>>>(AOC_YEAR, 19) {
    override fun getInput(handler: InputHandler): Pair<Set<String>, Collection<String>> {
        val (patternStr, designStr) = handler.getInput("\n\n")
        return patternStr.split(", ").toSet() to designStr.split("\n")
    }

    override fun solve(input: Pair<Set<String>, Collection<String>>): Pair<Int, Long> {
        val (patterns, designs) = input
        val cache: MutableMap<String, Long> = mutableMapOf("" to 1)
        fun count(design: String): Long {
            return cache.getOrPut(design) {
                patterns.filter(design::startsWith)
                    .map(design::removePrefix)
                    .sumOf(::count)
            }
        }
        val ways = designs.map(::count)
        return ways.filter { it > 0 }.size to ways.sum()
    }
}
