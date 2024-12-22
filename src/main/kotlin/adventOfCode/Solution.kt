package adventOfCode

import kotlin.math.max
import kotlin.system.measureNanoTime
import kotlin.time.Duration.Companion.nanoseconds

abstract class Solution<T>(year: Int, day: Int) {
    open val inputHandler = InputHandler(year, day)
    abstract fun getInput(handler: InputHandler): T
    abstract fun solve(input: T): Pair<Any?, Any?>
    open fun run(time: Boolean = true, threshold: Long = 500_000_000) {
        val input = getInput(inputHandler)
        var ret: Pair<Any?, Any?> = null to null
        var duration = 0L
        if (time) {
            duration = measureNanoTime {
                repeat(4) {
                    ret = solve(input)
                }
            }
            val projectedTime = duration / 4
            val trials = max(1, (threshold - duration) / projectedTime).toInt()
            duration += measureNanoTime {
                repeat(trials) {
                    solve(input)
                }
            }
            duration /= trials + 4
        } else {
            ret = solve(input)
        }
        println(ret.first)
        println(ret.second)
        if (time) println("Finished execution in ${(duration).nanoseconds}.")
    }
}
