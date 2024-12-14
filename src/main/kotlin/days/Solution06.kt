package days

import adventOfCode.InputHandler
import adventOfCode.Solution
import adventOfCode.util.PairOf
import adventOfCode.util.Point2D
import adventOfCode.util.plus

object Solution06 : Solution<Collection<String>>(AOC_YEAR, 6) {
    override fun getInput(handler: InputHandler) = handler.getInput("\n")

    private fun PairOf<Int>.turn() = when (this)
    {
        (-1 to 0) -> (0 to 1)
        (0 to 1) -> (1 to 0)
        (1 to 0) -> (0 to -1)
        (0 to -1) -> (-1 to 0)
        else -> throw IllegalArgumentException()
    }

    private operator fun Point2D.contains(point: Point2D) = point.first in 0..<this.first && point.second in 0..<this.second

    private fun simulatePatrol(walls: Set<Point2D>, start: Point2D, bounds: Point2D, trackEdges: Boolean): Set<Point2D>? {
        var pos = start
        var direction = -1 to 0
        val seen = mutableSetOf(pos to direction)
        while (true) {
            val newPos = pos + direction
            when {
                newPos in walls -> {
                    seen.add(pos to direction)
                    direction = direction.turn()
                }
                newPos !in bounds -> {
                    return seen.map { it.first }.toSet()
                }
                newPos to direction in seen -> {
                    return null
                }
                else -> {
                    pos = newPos
                    if (trackEdges) seen.add(pos to direction)
                }
            }
        }
    }

    override fun solve(input: Collection<String>): PairOf<Int> {
        val walls = mutableSetOf<Point2D>()
        var start = 0 to 0
        val bounds = input.size to input.first().length
        input.withIndex().forEach { (i, row) ->
            row.withIndex().forEach { (j, c) ->
                when (c) {
                    '#' -> walls.add(i to j)
                    '^' -> start = i to j
                }
            }
        }
        val seenPositions = simulatePatrol(walls, start, bounds, true)!!
        return seenPositions.size to seenPositions.count { pos -> simulatePatrol(walls.union(setOf(pos)), start, bounds, false) == null }
    }
}
