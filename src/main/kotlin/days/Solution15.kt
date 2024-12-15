package days

import adventOfCode.InputHandler
import adventOfCode.Solution
import adventOfCode.util.PairOf
import adventOfCode.util.Point2D
import adventOfCode.util.plus

object Solution15 : Solution<PairOf<String>>(AOC_YEAR, 15) {
    override fun getInput(handler: InputHandler) = handler.getInput("\n\n").zipWithNext().single()

    private val UP = -1 to 0
    private val RIGHT = 0 to 1
    private val DOWN = 1 to 0
    private val LEFT = 0 to -1
    private val DIRECTIONS = mapOf('^' to UP, '>' to RIGHT, 'v' to DOWN, '<' to LEFT)

    private val Point2D.gps get() = 100 * this.first + this.second

    private fun parseGrid(grid: List<String>): Triple<Set<Point2D>, Set<Point2D>, Point2D> {
        val walls: MutableSet<Point2D> = mutableSetOf()
        val boxes: MutableSet<Point2D> = mutableSetOf()
        var robot = 0 to 0
        grid.withIndex().forEach { (i, row) ->
            row.withIndex().forEach { (j, c) ->
                when (c) {
                    '#' -> walls.add(i to j)
                    'O', '[' -> boxes.add(i to j)
                    '@' -> robot = i to j
                }
            }
        }
        return Triple(walls, boxes, robot)
    }

    private fun step(walls: Set<Point2D>, boxes: Set<Point2D>, robot: Point2D, direction: Point2D): Pair<Set<Point2D>, Point2D>? {
        return when (val nextRobot = robot + direction) {
            in walls -> null
            !in boxes -> boxes to nextRobot
            else -> {
                var end = nextRobot
                while (end in boxes) end += direction
                if (end in walls) null else boxes - nextRobot + end to nextRobot
            }
        }
    }

    private fun step2(walls: Set<Point2D>, boxes: Set<Point2D>, robot: Point2D, direction: Point2D): Pair<Set<Point2D>, Point2D>? {
        val nextPos = robot + direction
        return when {
            nextPos in walls -> null
            nextPos !in boxes && nextPos + LEFT !in boxes -> boxes to nextPos
            else -> {
                val firstBox = if (nextPos in boxes) nextPos else nextPos + LEFT
                val movedBoxes = mutableSetOf(firstBox)
                val queue = mutableListOf(firstBox)
                while (queue.isNotEmpty()) {
                    val movedBox = queue.removeLast() + direction
                    if (movedBox in walls || movedBox + RIGHT in walls) return null
                    sequenceOf(movedBox, movedBox + LEFT, movedBox + RIGHT)
                        .filter { it in boxes && movedBoxes.add(it) }
                        .forEach { queue.add(it) }
                }
                return boxes - movedBoxes + movedBoxes.map { it + direction} to nextPos
            }
        }
    }

    override fun solve(input: PairOf<String>): PairOf<Int> {
        val (gridStr, steps) = input
        val newGridStr = sequenceOf("#" to "##", "O" to "[]", "." to "..", "@" to "@.")
            .fold(gridStr) { acc, (before, after) -> acc.replace(before, after) }
        var (walls, boxes, robot) = parseGrid(gridStr.split("\n"))
        var (walls2, boxes2, robot2) = parseGrid(newGridStr.split("\n"))
        steps.mapNotNull { DIRECTIONS[it] }.forEach { direction ->
            step(walls, boxes, robot, direction)?.let { boxes = it.first; robot = it.second }
            step2(walls2, boxes2, robot2, direction)?.let { boxes2 = it.first; robot2 = it.second }
        }
        return boxes.sumOf { it.gps } to boxes2.sumOf { it.gps }
    }
}
