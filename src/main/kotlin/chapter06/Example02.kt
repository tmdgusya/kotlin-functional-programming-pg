package chapter06

import fold_practice.FList
import kotlin.math.abs

typealias Rand<A> = (RNG) -> Pair<A, RNG>

fun <A> unit(a: A): Rand<A> = { rng: RNG -> a to rng }

fun <A, B> map(s: Rand<A>, f: (A) -> B): Rand<B> = { rng ->
    val (v1, ctx) = s(rng)
    f(v1) to ctx
}

// Example 6.1
fun nonNegativeInt(rng: RNG): Pair<Int, RNG> {
    val (value, ctx) = rng.nextInt()
    return (value.takeIf { it < 0 }?.let { abs(it + 1) } ?: value) to ctx
}

// Example 6.2
fun double(rng: RNG): Pair<Double, RNG> {
    val (i, rng2) = nonNegativeInt(rng)
    return (i % (Int.MAX_VALUE.toDouble() + 1)) to rng2
}

// Example 6.3
fun intDouble(rng: RNG): Pair<Pair<Int, Double>, RNG> = rng.nextInt().let { (i, ctx) ->
    (i to double(rng).first) to ctx
}

fun doubleInt(rng: RNG): Pair<Pair<Double, Int>, RNG> = rng.nextInt().let { (i, ctx) ->
    (double(rng).first to i) to ctx
}

// Example 6.4
fun ints(count: Int, rng: RNG): Pair<FList<Int>, RNG> = if (count > 0) {
    val (v, ctx) = rng.nextInt()
    val (l, ctx2) = ints(count - 1, ctx)
    FList(v, l) to ctx2
} else FList<Int>() to rng

// Example 6.5
fun doubleR(): Rand<Double> = map(::nonNegativeInt) { it.toDouble() }

// Example 6.6
fun <A, B, C> map2(ra: Rand<A>, rb: Rand<B>, f: (A, B) -> C): Rand<C> =  { rng: RNG ->
    val (r1, _) = ra(rng)
    val (r2, ctx2) = rb(rng)
    f(r1, r2) to ctx2
}

// Example 6.8
fun <A, B> flatMap(f: Rand<A>, g: (A) -> Rand<B>): Rand<B> = { rng: RNG ->
    val (r1, ctx) = f(rng)
    g(r1)(ctx)
}

// Example 6.9
fun <A, B> map2(s: Rand<A>, f: (A) -> B): Rand<B> = flatMap(s) { a -> unit(f(a)) }

fun <A, B, C> map3(ra: Rand<A>, rb: Rand<B>, f: (A, B) -> C): Rand<C> = flatMap(ra) { a -> map2(rb) { b -> f(a,b) } }

fun main() {
    println(double(SimpleRNG((Int.MAX_VALUE - 1).toLong()))) // (1.731576866E9, SimpleRNG(seed=113480621515569))
}