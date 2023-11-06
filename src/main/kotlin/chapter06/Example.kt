package chapter06

interface RNG {
    fun nextInt(): Pair<Int, RNG>
}

data class SimpleRNG(val seed: Long): RNG {
    override fun nextInt(): Pair<Int, RNG> {
        val newSeed = (seed * 0x5DEECE6DL + 0xBL) and 0xFFFFFFFFFFFFL
        val nextRNG = SimpleRNG(newSeed)
        val n = (newSeed ushr 16).toInt()
        return n to nextRNG
    }
}

fun main() {
    val rng = SimpleRNG(42)
    val (n1, rng2) = rng.nextInt()
    println("$n1, $rng2")
    val (n2, rng3) = rng2.nextInt()
    println("$n2, $rng3")
    val (n3, rng4) = rng3.nextInt()
    println("$n3, $rng4")
}