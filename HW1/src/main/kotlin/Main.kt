import kotlin.math.*

object Prime {
    private val isPrimeMap = HashMap<Long, Boolean>()
    private fun Long.hasNoNontrivialDivisors() = (2L..sqrt(this.toDouble()).toLong())
        .all { this % it != 0L }

    fun sequence(s: Sequence<Long>): Sequence<Long> = s
        .filter {
            isPrimeMap.getOrPut(it) {
                (it.hasNoNontrivialDivisors())
                        && (it > 1)
            }
        }
}

fun main() {}