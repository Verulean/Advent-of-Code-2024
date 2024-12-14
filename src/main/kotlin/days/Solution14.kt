package days

import adventOfCode.InputHandler
import adventOfCode.Solution
import adventOfCode.util.*

object Solution14 : Solution<PairOf<List<Point2D>>>(AOC_YEAR, 14) {
    override fun getInput(handler: InputHandler) = handler.getInput("\n").map {
        val (x, y, vx, vy) = it.ints()
        (x to y) to (vx to vy)
    }.unzip()

    private val gridSize = 101 to 103
    private val xMid = gridSize.first / 2
    private val yMid = gridSize.second / 2

    private operator fun Point2D.rem(other: Point2D): Point2D {
        val (x1, y1) = this
        val (x2, y2) = other
        return (x1 % x2 + x2) % x2 to (y1 % y2 + y2) % y2
    }
    private val Point2D.quadrant get() = when
    {
        this.first < xMid && this.second < yMid -> 0
        this.first < xMid && this.second > yMid -> 1
        this.first > xMid && this.second < yMid -> 2
        this.first > xMid && this.second > yMid -> 3
        else -> null
    }

    private fun List<Point2D>.step(velocities: List<Point2D>, n: Int = 1) = this.zip(velocities).map { (it.first + n * it.second) % gridSize }
    private val List<Point2D>.safetyFactor get() = this.mapNotNull { it.quadrant }
        .groupingBy { it }
        .eachCount()
        .values
        .fold(1L) { acc, count -> acc * count }
    private val List<Point2D>.isUnique get(): Boolean {
        val seen: MutableSet<Point2D> = mutableSetOf()
        this.forEach {
            if (!seen.add(it)) return false
        }
        return true
    }

    override fun solve(input: PairOf<List<Point2D>>): Pair<Long, Int> {
        var points = input.first
        val velocities = input.second
        points = points.step(velocities, 100)
        val ans1 = points.safetyFactor
        var t = 100
        while (!points.isUnique) {
            points = points.step(velocities)
            t += 1
        }
        return ans1 to t
    }
}
