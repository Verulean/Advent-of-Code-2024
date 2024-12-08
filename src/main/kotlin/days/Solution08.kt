package days

import adventOfCode.InputHandler
import adventOfCode.Solution
import adventOfCode.util.PairOf
import adventOfCode.util.Point2D
import adventOfCode.util.minus
import adventOfCode.util.plus

private typealias MapBounds = PairOf<IntRange>
private typealias AntinodeFinder = (Point2D, Point2D, Point2D, MapBounds) -> Sequence<Point2D>

object Solution08 : Solution<List<String>>(AOC_YEAR, 8) {
    override fun getInput(handler: InputHandler) = handler.getInput("\n")

    private fun Point2D.isInBounds(bounds: MapBounds) = this.first in bounds.first && this.second in bounds.second

    private fun findAntinodes(antennae: List<Point2D>, bounds: MapBounds, f: AntinodeFinder) = antennae.flatMapIndexed { i, p1 ->
        antennae.drop(i + 1).flatMap { p2 -> f(p1, p2, p2 - p1, bounds) }
    }

    private fun find1(p1: Point2D, p2: Point2D, delta: Point2D, bounds: MapBounds) = sequenceOf(p1 - delta, p2 + delta).filter { it.isInBounds(bounds) }

    private fun find2(p1: Point2D, p2: Point2D, delta: Point2D, bounds: MapBounds) =
        generateSequence(p1) { p -> p - delta }.takeWhile { it.isInBounds(bounds) } +
            generateSequence(p2) { p -> p + delta }.takeWhile { it.isInBounds(bounds) }

    override fun solve(input: List<String>): PairOf<Int> {
        val bounds = input.indices to input.first().indices
        val antennaMap = input.flatMapIndexed { i, line ->
            line.mapIndexedNotNull { j, c -> if (c == '.') null else c to (i to j) }
        }.groupBy({ it.first }, { it.second })
        fun countAntinodes(f: AntinodeFinder) = antennaMap.values.flatMap { findAntinodes(it, bounds, f) }.toSet().size
        return countAntinodes(::find1) to countAntinodes(::find2)
    }
}
