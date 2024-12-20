package days

import adventOfCode.InputHandler
import adventOfCode.Solution
import adventOfCode.util.PairOf
import adventOfCode.util.Point2D
import adventOfCode.util.plus
import kotlin.math.abs

object Solution20 : Solution<Triple<Set<Point2D>, Point2D, Point2D>>(AOC_YEAR, 20) {
    override fun getInput(handler: InputHandler): Triple<Set<Point2D>, Point2D, Point2D> {
        val walls: MutableSet<Point2D> = mutableSetOf()
        var start = 0 to 0
        var end = 0 to 0
        handler.getInput("\n").withIndex().forEach { (i, row) ->
            row.withIndex().forEach { (j, c) ->
                when (c) {
                    '#' -> walls.add(i to j)
                    'S' -> start = i to j
                    'E' -> end = i to j
                }
            }
        }
        return Triple(walls, start, end)
    }

    private fun Set<Point2D>.getPaths(start: Point2D, end: Point2D): Collection<List<Point2D>> {
        val seen: MutableSet<Point2D> = mutableSetOf()
        val paths: MutableList<List<Point2D>> = mutableListOf()
        val q = ArrayDeque<List<Point2D>>()
        q.add(listOf(start))
        while (q.isNotEmpty()) {
            val path = q.removeFirst()
            val curr = path.last()
            when {
                curr == end -> paths.add(path)
                !seen.add(curr) -> continue
                else -> {
                    sequenceOf(-1 to 0, 0 to 1, 1 to 0, 0 to -1).map { curr + it }
                        .filter { it !in this }
                        .forEach { q.add(path + it) }
                }
            }
        }
        return paths
    }

    private fun Collection<List<Point2D>>.getCheats(maxCheatTime: Int): Map<Int, Set<PairOf<Point2D>>> {
        val cheaters: MutableMap<Int, MutableSet<PairOf<Point2D>>> = mutableMapOf()
        this.forEach { path ->
            val inversePath = path.withIndex().associate { (t, p) -> p to t }
            path.withIndex().forEach { (t, p) ->
                (-maxCheatTime..maxCheatTime).forEach { di ->
                    val yCheat = maxCheatTime - abs(di)
                    (-yCheat..yCheat).forEach { dj ->
                        val dt = abs(di) + abs(dj)
                        val p2 = p + (di to dj)
                        if (inversePath.getOrDefault(p2, 0) - t >= 100 + dt) {
                            cheaters.getOrPut(dt) { mutableSetOf() }.add(p to p2)
                        }
                    }
                }
            }
        }
        return cheaters
    }

    override fun solve(input: Triple<Set<Point2D>, Point2D, Point2D>): PairOf<Int> {
        val (walls, start, end) = input
        val cheats = walls.getPaths(start, end).getCheats(20)
        return (cheats[2]?.size ?: 0) to cheats.values.sumOf { it.size }
    }
}
