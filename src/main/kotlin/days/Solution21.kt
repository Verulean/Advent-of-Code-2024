package days

import adventOfCode.InputHandler
import adventOfCode.Solution
import adventOfCode.util.PairOf
import adventOfCode.util.Point2D
import adventOfCode.util.TripleOf
import adventOfCode.util.plus

object Solution21 : Solution<Collection<String>>(AOC_YEAR, 21) {
    override fun getInput(handler: InputHandler) = handler.getInput("\n")

    private val UP = -1 to 0
    private val RIGHT = 0 to 1
    private val DOWN = 1 to 0
    private val LEFT = 0 to -1

    private val numpadInfo = (3 to 0) to mapOf(
        '7' to (0 to 0), '8' to (0 to 1), '9' to (0 to 2),
        '4' to (1 to 0), '5' to (1 to 1), '6' to (1 to 2),
        '1' to (2 to 0), '2' to (2 to 1), '3' to (2 to 2),
                         '0' to (3 to 1), 'A' to (3 to 2)
    )

    private val keypadInfo = (0 to 0) to mapOf(
                         '^' to (0 to 1), 'A' to (0 to 2),
        '<' to (1 to 0), 'v' to (1 to 1), '>' to (1 to 2)
    )

    val keypadMemo: MutableMap<TripleOf<Point2D>, Set<String>> = mutableMapOf()
    private fun getKeypadInputs(start: Point2D, end: Point2D, ignore: Point2D) = keypadMemo.getOrPut(Triple(start, end, ignore)) {
        val paths: MutableSet<String> = mutableSetOf()
        val q = ArrayDeque<Pair<Point2D, String>>()
        q.add(start to "")
        while (q.isNotEmpty()) {
            val (pos, input) = q.removeFirst()
            if (pos == end) {
                paths.add(input + 'A')
                continue
            }
            if (pos == ignore) {
                continue
            }
            if (pos.first < end.first) {
                q.add(pos + DOWN to "${input}v")
            } else if (pos.first > end.first) {
                q.add(pos + UP to "$input^")
            }
            if (pos.second < end.second) {
                q.add(pos + RIGHT to "$input>")
            } else if (pos.second > end.second) {
                q.add(pos + LEFT to "$input<")
            }
        }
        return paths
    }

    private val shortestMemo: MutableMap<Triple<String, Int, Int>, Long> = mutableMapOf()
    private fun findShortest(code: String, robots: Int = 2, layer: Int = 0): Long {
        val memoKey = Triple(code, robots, layer)
        shortestMemo[memoKey]?.also { return it } // getOrPut doesn't work due to recursive call
        if (layer == robots + 1) return code.length.toLong()
        val (ignore, keyToPos) = if (layer > 0) keypadInfo else numpadInfo
        var ret = 0L
        var pos = keyToPos.getValue('A')
        code.forEach { key ->
            val nextPos = keyToPos.getValue(key)
            ret += getKeypadInputs(pos, nextPos, ignore).minOf { findShortest(it, robots, layer + 1) }
            pos = nextPos
        }
        shortestMemo[memoKey] = ret
        return ret
    }

    override fun solve(input: Collection<String>): PairOf<Long> {
        return input.fold(0L to 0L) { (sum1, sum2), code ->
            val numericPiece = code.trimStart('0').trimEnd('A').toLong()
            sum1 + numericPiece * findShortest(code) to
                sum2 + numericPiece * findShortest(code, 25)
        }
    }
}
