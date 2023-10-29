package chapter05

import chapter04.Option
import fold_practice.FList
import kotlin.math.E

sealed class FStream<out A> {
    companion object {

        fun <A> of(vararg xs: A): FStream<A> =
            if (xs.isEmpty()) this() else this({ xs[0] }, { of(*xs.sliceArray(1 until xs.size)) })

        operator fun <A> invoke(head: () -> A, tail: () -> FStream<A>): FStream<A> {
            val hd: A by lazy(head)
            val tl: FStream<A> by lazy(tail)
            return Cons({ hd }, { tl })
        }

        operator fun <A> invoke(): FStream<A> = Empty
    }
}

data object Empty : FStream<Nothing>()

data class Cons<out A>(
    // thunk for head
    val head: () -> A,
    val tail: () -> FStream<A>
) : FStream<A>()

fun <A> FStream<A>.headOption(): Option<A> = when (this) {
    is Empty -> Option.None
    is Cons -> Option.Some(head())
}


// Example 5.1
fun <A> FStream<A>.toList(): FList<A> = when (this) {
    is Empty -> FList.Nil
    is Cons -> FList(head(), tail().toList())
}

// Example 5.2
fun <A> FStream<A>.take(n: Int): FStream<A> = when (this) {
    is Empty -> FStream()
    is Cons -> if (n == 0) FStream() else FStream(head) { this.tail().take(n - 1) }
}

fun <A> FStream<A>.drop(n: Int): FStream<A> = when (this) {
    is Empty -> FStream()
    is Cons -> if (n > 0) this.tail().drop(n - 1) else this
}

// Example 5.3
fun <A> FStream<A>.dropWhile(p: (A) -> Boolean): FStream<A> = when (this) {
    is Empty -> this
    is Cons -> if (p(head())) FStream(head) { this.tail().dropWhile(p) } else this.tail().dropWhile(p)
}

fun main() {
    val fstream: FStream<Int> = FStream.of(1, 2, 3, 4, 5)
    println(fstream.toList())
    println(fstream.take(4).toList())
    println(fstream.drop(2).toList())
    println(fstream.dropWhile { it % 2 == 1 }.toList())
}