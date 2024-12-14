package days

import adventOfCode.InputHandler
import adventOfCode.Solution
import adventOfCode.util.PairOf
import adventOfCode.util.Point2D
import adventOfCode.util.ints
import adventOfCode.util.plus

object Solution14 : Solution<PairOf<List<Point2D>>>(AOC_YEAR, 14) {
    override fun getInput(handler: InputHandler) = handler.getInput("\n").map {
        val (x, y, vx, vy) = it.ints()
        (x to y) to (vx to vy)
    }.unzip()

    private const val X_DIMENSION = 101
    private const val Y_DIMENSION = 103
    private const val X_MID = X_DIMENSION / 2
    private const val Y_MID = Y_DIMENSION / 2
    private const val MODULAR_INVERSE = 51

    private operator fun Point2D.rem(other: Point2D): Point2D {
        val (x1, y1) = this
        val (x2, y2) = other
        return (x1 % x2 + x2) % x2 to (y1 % y2 + y2) % y2
    }

    private val Point2D.quadrant get() = when
    {
        this.first < X_MID && this.second < Y_MID -> 0
        this.first < X_MID && this.second > Y_MID -> 1
        this.first > X_MID && this.second < Y_MID -> 2
        this.first > X_MID && this.second > Y_MID -> 3
        else -> null
    }

    private fun List<Point2D>.step(velocities: List<Point2D>) = this.zip(velocities).map { (it.first + it.second) % (X_DIMENSION to Y_DIMENSION) }

    private val List<Point2D>.safetyFactor get() = this.mapNotNull { it.quadrant }
        .groupingBy { it }
        .eachCount()
        .values
        .fold(1) { acc, count -> acc * count }

    private val List<Int>.variance get(): Double {
        val mean = this.average()
        return this.map { it - mean }.map { it * it }.average()
    }

    override fun solve(input: PairOf<List<Point2D>>): PairOf<Int> {
        var points = input.first
        val velocities = input.second
        var ans1 = 0
        var bestX = 10_000.0 to 0
        var bestY = 10_000.0 to 0
        (1..Y_DIMENSION).forEach { t ->
            points = points.step(velocities)
            if (t == 100) ans1 = points.safetyFactor
            val xVariance = points.map { it.first }.variance
            if (xVariance < bestX.first) bestX = xVariance to t
            val yVariance = points.map { it.second }.variance
            if (yVariance < bestY.first) bestY = yVariance to t
        }
        val ans2 = (MODULAR_INVERSE * (bestY.second * X_DIMENSION + bestX.second * Y_DIMENSION)) % (X_DIMENSION * Y_DIMENSION)
        return ans1 to ans2
    }
}
