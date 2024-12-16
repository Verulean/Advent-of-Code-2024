package days

import adventOfCode.InputHandler
import adventOfCode.Solution
import adventOfCode.util.PairOf
import adventOfCode.util.Point2D
import adventOfCode.util.minus
import adventOfCode.util.plus
import java.util.*
import kotlin.collections.ArrayDeque

private typealias State = PairOf<Point2D>

private class QueueMember(val cost: Int, val state: State): Comparable<QueueMember> {
    override fun compareTo(other: QueueMember) = cost.compareTo(other.cost)
    operator fun component1() = this.cost
    operator fun component2() = this.state
}

object Solution16 : Solution<Triple<Set<Point2D>, Point2D, Point2D>>(AOC_YEAR, 16) {
    private fun Point2D.turnLeft() = -this.second to this.first
    private fun Point2D.turnRight() = this.second to -this.first

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

    private fun State.neighbors(walls: Set<Point2D>): Sequence<Pair<Int, State>> {
        val (pos, dir) = this
        val stepped = pos + dir
        return if (stepped in walls) sequenceOf(1000 to (pos to dir.turnLeft()), 1000 to (pos to dir.turnRight()))
            else sequenceOf(1 to (stepped to dir), 1000 to (pos to dir.turnLeft()), 1000 to (pos to dir.turnRight()))
    }

    private fun State.srobhgien(walls: Set<Point2D>): Sequence<Pair<Int, State>> {
        val (pos, dir) = this
        val unstepped = pos - dir
        return if (unstepped in walls) sequenceOf(-1000 to (pos to dir.turnRight()), -1000 to (pos to dir.turnLeft()))
        else sequenceOf(-1 to (unstepped to dir), -1000 to (pos to dir.turnRight()), -1000 to (pos to dir.turnLeft()))
    }

    private fun dijkstra(walls: Set<Point2D>, startState: State, end: Point2D): Pair<Map<State, Int>, Int> {
        val bestCost = mutableMapOf(startState to 0)
        val seenStates: MutableSet<State> = mutableSetOf()
        val q = PriorityQueue<QueueMember>()
        q.add(QueueMember(0, startState))
        while (q.isNotEmpty()) {
            val (cost, state) = q.remove()
            if (!seenStates.add(state)) continue
            if (state.first == end) {
                return bestCost to cost
            }
            state.neighbors(walls).forEach { (dCost, newState) ->
                val newCost = cost + dCost
                if (newState !in bestCost || newCost < bestCost.getValue(newState)) {
                    bestCost[newState] = newCost
                    q.add(QueueMember(newCost, newState))
                }
            }
        }
        error("Unreachable")
    }

    private fun countBests(walls: Set<Point2D>, bestCost: Map<State, Int>, end: Point2D, ans1: Int): Int {
        val bestStates: MutableSet<State> = mutableSetOf()
        val q = ArrayDeque<State>()
        sequenceOf(-1 to 0, 0 to 1, 1 to 0, 0 to -1).map { end to it }
            .filter { bestCost[it] == ans1 }
            .forEach {
                bestStates.add(it)
                q.add(it)
            }
        while (q.isNotEmpty()) {
            val state = q.removeFirst()
            val cost = bestCost.getValue(state)
            state.srobhgien(walls).forEach { (dCost, prevState) ->
                if (bestCost[prevState] == cost + dCost && bestStates.add(prevState)) {
                    q.add(prevState)
                }
            }
        }
        return bestStates.map { it.first }.toSet().size
    }

    override fun solve(input: Triple<Set<Point2D>, Point2D, Point2D>): Pair<Any?, Any?> {
        val (walls, start, end) = input
        val startState = start to (0 to 1)
        val (bestCost, ans1) = dijkstra(walls, startState, end)
        return ans1 to countBests(walls, bestCost, end, ans1)
    }
}
