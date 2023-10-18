package chapter04

import fold_practice.FList
import kotlin.math.pow

sealed class Option<out A> {
    data class Some<out A>(val get: A) : Option<A>()
    data object None : Option<Nothing>()

    companion object {
        operator fun <A> invoke(a: A): Option<A> = Some(a)
        operator fun <A> invoke(): Option<A> = None
    }


    inline fun <B> map(f: (A) -> B): Option<B> = this.flatMap { Option(f(it)) }

    inline fun <B> flatMap(f: (A) -> Option<B>): Option<B> = if (this is Some) f(get) else None

    inline fun getOrElse(ob: () -> @UnsafeVariance A): A = if (this is Some) get else ob()

    inline fun orElse(default: () -> Option<@UnsafeVariance A>): Option<A> = this.map { Option(it) }.getOrElse(default)

    inline fun filter(f: (A) -> Boolean): Option<A> = this.flatMap {
        it.takeIf(f)?.let(::Some) ?: None
    }
}

fun <A, B> lift(f: (A) -> B): (Option<A>) -> Option<B> = { oa -> oa.map(f) }

// Example 4.2
fun variance(xs: List<Double>): Option<Double> = Option(xs.sum() to xs.size)
    .flatMap { (sum, size) ->
        Option(xs.sumOf { x -> (x - (sum / size)).pow(2) } / size)
    }

// Example 4.3
fun <A, B, C> Option<A>.map2(other: Option<B>, f: (A, B) -> C): Option<C> = this.flatMap { a ->
    other.map { b -> f(a, b) }
}

// Example 4.4
fun <A> FList<Option<A>>.sequence(): Option<FList<A>> =
    this.foldRight(acc = Option(FList())) { a: Option<A>, acc: Option<FList<A>> ->
        a.map2(acc) { a1, fList -> FList(a1, fList) }
    }

// Example 4.5
fun <A, B> traverse(
    xa: FList<A>,
    f: (A) -> Option<B>
): Option<FList<B>> = when (xa) {
    is FList.Nil -> Option.None
    is FList.FCons -> f(xa.head).map2(traverse(xa.tail, f)) { a, b -> FList(a, b) }
}




