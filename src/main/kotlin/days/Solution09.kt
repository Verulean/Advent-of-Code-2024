package days

import adventOfCode.InputHandler
import adventOfCode.Solution
import adventOfCode.util.PairOf
import java.util.*

private typealias DiskNumber = Long

object Solution09 : Solution<String>(AOC_YEAR, 9) {
    override fun getInput(handler: InputHandler) = handler.getInput()

    private fun part1(fileBlocks: Stack<PairOf<DiskNumber>>, freeBlocks: PriorityQueue<DiskNumber>): Long {
        var ret = 0L
        while (freeBlocks.isNotEmpty()) {
            val (i, id) = fileBlocks.pop()
            val j = freeBlocks.remove()
            if (j > i) {
                ret += i * id
                break
            }
            ret += j * id
            freeBlocks.add(i)
        }
        return ret + fileBlocks.sumOf { (i, id) -> i * id }
    }

    private fun findSpace(freeSegments: Map<DiskNumber, PriorityQueue<DiskNumber>>, length: DiskNumber) =
        (length..9L).mapNotNull { n ->
            freeSegments[n]?.peek()?.let { it to n }
        }.minByOrNull { it.first } ?: (0L to 0L)

    private fun segmentChecksum(id: Int, i: DiskNumber, n: DiskNumber) = (i * n + n * (n - 1) / 2) * id

    private fun part2(fileSegments: List<PairOf<DiskNumber>>, freeSegments: Map<DiskNumber, PriorityQueue<DiskNumber>>) =
        fileSegments.withIndex().reversed().sumOf { (id, value) ->
            val (i, length) = value
            val (j, freeLength) = findSpace(freeSegments, length)
            when {
                j == 0L || j >= i -> segmentChecksum(id, i, length)
                else -> {
                    freeSegments[freeLength]?.remove()
                    if (freeLength > length) {
                        freeSegments[freeLength - length]?.add(j + length)
                    }
                    segmentChecksum(id, j, length)
                }
            }
        }

    override fun solve(input: String): PairOf<Long> {
        val fileBlocks = Stack<PairOf<DiskNumber>>()
        val fileSegments: MutableList<PairOf<DiskNumber>> = mutableListOf()
        val freeBlocks = PriorityQueue<DiskNumber>()
        val freeSegments = (1L..9L).associateWith { PriorityQueue<DiskNumber>() }
        input.map { it.digitToInt().toLong() }.fold(Triple(0L, 0L, true)) { (i, id, isFile), n ->
            if (isFile) {
                fileBlocks.addAll((i..<i + n).map { it to id })
                fileSegments.add(i to n)
                Triple(i + n, id + 1, false)
            } else {
                freeBlocks.addAll(i..<i + n)
                freeSegments[n]?.add(i)
                Triple(i + n, id, true)
            }
        }
        return part1(fileBlocks, freeBlocks) to part2(fileSegments, freeSegments)
    }
}
