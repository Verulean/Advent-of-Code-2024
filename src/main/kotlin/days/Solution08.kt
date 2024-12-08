package days

import adventOfCode.InputHandler
import adventOfCode.Solution
import adventOfCode.util.PairOf
import adventOfCode.util.Point2D
import adventOfCode.util.minus
import adventOfCode.util.plus

private typealias MapBounds = PairOf<IntRange>

object Solution08 : Solution<List<String>>(AOC_YEAR, 8) {
    override fun getInput(handler: InputHandler) = handler.getInput("\n")

    private fun Point2D.isInBounds(bounds: MapBounds) = this.first in bounds.first && this.second in bounds.second

    private fun findAntinodes(antennae: List<Point2D>, bounds: MapBounds) = antennae.flatMapIndexed { i, p1 ->
        antennae.drop(i + 1).flatMap { p2 ->
            val delta = p2 - p1
            sequenceOf(p1 - delta, p2 + delta)
        }
    }.filter { it.isInBounds(bounds) }

    private fun findAntinodes2(antennae: List<Point2D>, bounds: MapBounds) = antennae.flatMapIndexed { i, p1 ->
        antennae.drop(i + 1).flatMap { p2 ->
            val delta = p2 - p1
            val antinodes = mutableSetOf(p1, p2)
            var p = p1 - delta
            while (p.isInBounds(bounds)) {
                antinodes.add(p)
                p -= delta
            }
            p = p2 + delta
            while (p.isInBounds(bounds)) {
                antinodes.add(p)
                p += delta
            }
            antinodes
        }
    }

    override fun solve(input: List<String>): PairOf<Int> {
        val bounds = input.indices to input.first().indices
        val antennaMap = input.flatMapIndexed { i, line ->
            line.mapIndexedNotNull { j, c -> if (c == '.') null else c to (i to j) }
        }.groupBy({ it.first }, { it.second })
        fun antinodeCount(f: (antennae: List<Point2D>, bounds: MapBounds) -> Collection<Point2D>) = antennaMap.values.flatMap { f(it, bounds) }.toSet().size
        return antinodeCount(::findAntinodes) to antinodeCount(::findAntinodes2)
    }
}
