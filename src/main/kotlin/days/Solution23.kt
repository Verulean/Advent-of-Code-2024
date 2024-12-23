package days

import adventOfCode.InputHandler
import adventOfCode.Solution

private typealias ConnectionMap = Map<String, Set<String>>

object Solution23 : Solution<ConnectionMap>(AOC_YEAR, 23) {
    override fun getInput(handler: InputHandler): ConnectionMap {
        val connections = mutableMapOf<String, MutableSet<String>>()
        handler.getInput("\n").forEach {
            val (a, b) = it.split("-")
            connections.getOrPut(a) { mutableSetOf() }.add(b)
            connections.getOrPut(b) { mutableSetOf() }.add(a)
        }
        return connections
    }

    private fun ConnectionMap.getTriples() = this.entries.filter { it.key.startsWith('t') }
        .flatMap { (c1, adjs) ->
            adjs.flatMap { c2 ->
                (adjs intersect this.getValue(c2)).map { c3 -> setOf(c1, c2, c3) }
            }
        }.toSet()

    private fun ConnectionMap.getMaxClique() = this.entries.withIndex().flatMap { (i, entry) ->
        val (c1, adjs) = entry
        this.keys.drop(i + 1).filter { it in adjs }.map { c2 ->
            val clique = mutableSetOf<String>()
            val q = ArrayDeque<String>()
            q.addAll(sequenceOf(c1, c2))
            while (q.isNotEmpty()) {
                val curr = q.removeFirst()
                when {
                    (clique - this.getValue(curr)).isNotEmpty() -> continue
                    !clique.add(curr) -> continue
                    else -> q.addAll(this.getValue(curr) - clique)
                }
            }
            clique
        }
    }.maxBy { it.size }

    override fun solve(input: ConnectionMap): Pair<Int, String> {
        return input.getTriples().size to input.getMaxClique().sorted().joinToString(",")
    }
}
