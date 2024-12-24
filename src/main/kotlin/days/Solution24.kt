package days

import adventOfCode.InputHandler
import adventOfCode.Solution
import adventOfCode.util.PairOf
import adventOfCode.util.TripleOf

private typealias Wires = Map<String, TripleOf<String>>

object Solution24 : Solution<Pair<MutableMap<String, Boolean>, Wires>>(AOC_YEAR, 24) {
    override fun getInput(handler: InputHandler): Pair<MutableMap<String, Boolean>, Wires> {
        val (valueStr, wireStr) = handler.getInput("\n\n")
        val valueMap = valueStr.split("\n").associate {
            val (name, value) = it.split(": ")
            name to (value == "1")
        }.toMutableMap()
        val wireMap = wireStr.split("\n").associate {
            val (a, op, b, _, c) = it.split(" ")
            c to Triple(a, op, b)
        }
        return valueMap to wireMap
    }

    private val xy = setOf('x', 'y')

    private fun solveWires(wires: Wires, values: MutableMap<String, Boolean>): Long {
        var q = wires.map { it.key to it.value }
        while (q.isNotEmpty()) {
            q = q.filter { (c, gate) ->
                val (a, op, b) = gate
                if (a !in values) return@filter true
                if (b !in values) return@filter true
                val va = values.getValue(a)
                val vb = values.getValue(b)
                when (op) {
                    "XOR" -> values[c] = va xor vb
                    "AND" -> values[c] = va and vb
                    "OR" -> values[c] = va or vb
                }
                return@filter false
            }
        }
        return values.entries.filter { it.key.startsWith('z') }
            .sortedByDescending { it.key }
            .map { if (it.value) '1' else '0' }
            .joinToString("")
            .toLong(2)
    }

    private fun findSwaps(wires: Wires): String {
        val xyXors = mutableMapOf<Int, String>()
        val xyAnds = mutableMapOf<Int, String>()
        val xorWires = mutableMapOf<String, String>()
        val andWires = mutableMapOf<String, String>()
        val orArgWires = mutableMapOf<String, PairOf<String>>()
        wires.forEach { (c, gate) ->
            val (a, op, b) = gate
            when (op) {
                "XOR" -> {
                    xorWires[a] = c
                    xorWires[b] = c
                    if (a.first() in xy) xyXors[a.drop(1).toInt()] = c
                }
                "AND" -> {
                    andWires[a] = c
                    andWires[b] = c
                    if (a.first() in xy) xyAnds[a.drop(1).toInt()] = c
                }
                "OR" -> {
                    orArgWires[a] = b to c
                    orArgWires[b] = a to c
                }
            }
        }
        val swaps = mutableSetOf<String>()
        xyXors.filter { it.key > 0 }.forEach { (i, xXorY) ->
            val z = String.format("z%02d", i)
            val xAndY = xyAnds.getValue(i)
            val (zA, zOp, zB) = wires.getValue(z)
            when {
                z == xXorY -> {
                    swaps.add(z)
                    val r = orArgWires.getValue(xAndY).first
                    val arg = wires.getValue(r).second
                    swaps.add(xorWires.getValue(arg))
                }
                z == xAndY -> {
                    swaps.add(z)
                    swaps.add(xorWires.getValue(xXorY))
                }
                zOp == "AND" -> {
                    swaps.add(z)
                    swaps.add(xorWires.getValue(zA))
                }
                zOp == "OR" -> {
                    swaps.add(z)
                    swaps.add(xorWires.getValue(xXorY))
                }
                else -> {
                    val cAndM = andWires.getValue(zA)
                    if (cAndM !in orArgWires) {
                        swaps.add(cAndM)
                        swaps.add(if (cAndM == zA || cAndM == zB) xXorY else orArgWires.getValue(xAndY).second)
                    } else {
                        val (n, c) = orArgWires.getValue(cAndM)
                        when (n) {
                            c -> {
                                swaps.add(xAndY)
                                swaps.add(n)
                            }
                            xXorY -> {
                                swaps.add(xAndY)
                                swaps.add(xXorY)
                            }
                        }
                    }
                }
            }
        }
        return swaps.sorted().joinToString(",")
    }

    override fun solve(input: Pair<MutableMap<String, Boolean>, Wires>): Pair<Long, String> {
        val (values, wires) = input
        return solveWires(wires, values) to findSwaps(wires)
    }
}
