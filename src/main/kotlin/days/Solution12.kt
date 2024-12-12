package days

import adventOfCode.InputHandler
import adventOfCode.Solution
import adventOfCode.util.PairOf
import adventOfCode.util.Point2D
import adventOfCode.util.plus
import java.util.*

private typealias Grid = List<String>

object Solution12 : Solution<Grid>(AOC_YEAR, 12) {
    override fun getInput(handler: InputHandler) = handler.getInput("\n")

    private val directions = arrayOf(-1 to 0, 0 to 1, 1 to 0, 0 to -1)

    private fun PairOf<Int>.rotate() = this.second to -this.first
    private operator fun Grid.get(p: Point2D) = this.getOrNull(p.first)?.getOrNull(p.second)

    private fun floodfill(grid: Grid, start: Point2D): Set<Point2D> {
        val c = grid[start]
        val points = mutableSetOf(start)
        val q = Stack<Point2D>()
        q.add(start)
        while (q.isNotEmpty()) {
            val p = q.pop()
            directions.forEach { d ->
                val neighbor = p + d
                if (neighbor !in points && grid[neighbor] == c) {
                    points.add(neighbor)
                    q.add(neighbor)
                }
            }
        }
        return points
    }

    private fun scoreRegion(points: Set<Point2D>): PairOf<Int> {
        val area = points.size
        val (perimeter1, perimeter2) = points.flatMap { p -> directions.map { p to it } }
            .filter { it.first + it.second !in points }
            .fold(0 to 0) { acc, (p, d) ->
                val neighbor = p + d.rotate()
                when {
                    neighbor !in points || neighbor + d in points -> acc + (1 to 1)
                    else -> acc + (1 to 0)
                }
            }
        return area * perimeter1 to area * perimeter2
    }

    override fun solve(input: Grid): PairOf<Int> {
        val seen: MutableSet<Point2D> = mutableSetOf()
        var ans = 0 to 0
        input.withIndex().forEach { (i, row) ->
            row.indices.map { i to it }
                .forEach { start ->
                    if (start !in seen) {
                        val points = floodfill(input, start)
                        seen.addAll(points)
                        ans += scoreRegion(points)
                    }
                }
        }
        return ans
    }
}
