package days

import adventOfCode.InputHandler
import adventOfCode.Solution
import adventOfCode.util.PairOf
import adventOfCode.util.Point2D
import adventOfCode.util.plus

object Solution10 : Solution<List<List<Int>>>(AOC_YEAR, 10) {
    override fun getInput(handler: InputHandler) = handler.getInput("\n").map { row -> row.map { it.digitToInt() } }

    private operator fun PairOf<IntRange>.contains(p: Point2D) = p.first in this.first && p.second in this.second
    private fun List<List<Int>>.getOrNull(p: Point2D) = this.getOrNull(p.first)?.getOrNull(p.second)
    private val Point2D.neighbors get() = sequenceOf(-1 to 0, 0 to 1, 1 to 0, 0 to -1).map { this + it }

    private fun getPaths(grid: List<List<Int>>, start: Point2D) =
        (1..9).fold(setOf(listOf(start))) { paths, height ->
            paths.flatMap { path ->
                path.last().neighbors.filter { grid.getOrNull(it) == height }.map { path + it }
            }.toSet()
        }

    override fun solve(input: List<List<Int>>): PairOf<Int> {
        return input.indices.flatMap { i -> input.first().indices.map { j -> i to j } }
            .filter { input.getOrNull(it) == 0 }
            .fold(0 to 0) { acc, start ->
                val paths = getPaths(input, start)
                acc + (paths.map { it.last() }.toSet().size to paths.size)
            }
    }
}
