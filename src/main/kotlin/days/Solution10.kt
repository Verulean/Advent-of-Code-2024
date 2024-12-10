package days

import adventOfCode.InputHandler
import adventOfCode.Solution
import adventOfCode.util.PairOf
import adventOfCode.util.Point2D
import adventOfCode.util.plus

object Solution10 : Solution<List<List<Int>>>(AOC_YEAR, 10) {
    override fun getInput(handler: InputHandler) = handler.getInput("\n").map { row -> row.map { it.digitToInt() } }

    private operator fun PairOf<IntRange>.contains(p: Point2D) = p.first in this.first && p.second in this.second
    private operator fun List<List<Int>>.get(p: Point2D) = this[p.first][p.second]
    private val Point2D.neighbors get() = sequenceOf(-1 to 0, 0 to 1, 1 to 0, 0 to -1).map { this + it }

    private fun getPaths(grid: List<List<Int>>, start: Point2D, bounds: PairOf<IntRange>) =
        (1..9).fold(setOf(listOf(start))) { paths, height ->
            paths.flatMap { path ->
                path.last().neighbors.mapNotNull { if (it in bounds && grid[it] == height) path + it else null }
            }.toSet()
        }

    override fun solve(input: List<List<Int>>): PairOf<Int> {
        val bounds = input.indices to input.first().indices
        return bounds.first.flatMap { i -> bounds.second.map { j -> i to j } }
            .filter { input[it] == 0 }
            .fold(0 to 0) { acc, start ->
                val paths = getPaths(input, start, bounds)
                acc + (paths.map { it.last() }.toSet().size to paths.size)
            }
    }
}
