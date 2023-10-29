package chapter05

import chapter04.Option
import chapter3.foldRight
import fold_practice.FList

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

fun <A, B> FStream<A>.foldRight(
    z: () -> B,
    f: (A, () -> B) -> B
): B = when (this) {
    is Cons -> f(this.head()) {
        tail().foldRight(z, f)
    }

    is Empty -> z()
}

fun <A> FStream<A>.exist(p: (A) -> Boolean): Boolean = this.foldRight({ false }, { a, b -> p(a) || b() })


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
    is Cons -> lazyIf(
        cond = p(head()),
        onTrue = { FStream(head) { this.tail().dropWhile(p) } },
        onFalse = { this.tail().dropWhile(p) })
}

// Example 5.4
fun <A> FStream<A>.forAll(p: (A) -> Boolean): Boolean = this.foldRight({ true }, {a , b -> p(a) && b() })

// Example 5.5
fun <A> FStream<A>.takeWhile(p: (A) -> Boolean): FStream<A> = this.foldRight({ FStream() }) { a: A, b: () -> FStream<A> ->
    when (this) {
        is Empty -> FStream()
        is Cons -> lazyIf(p(a), onTrue = { FStream(head) { tail().takeWhile(p) } }, onFalse = { b() })
    }
}

// Example 5.6


// Example 5.7
fun <A, B> FStream<A>.map(func: (A) -> B): FStream<B> = when (this) {
    is Empty -> FStream()
    is Cons -> FStream({ func(head()) }, { tail().map(func) })
}

fun <A> FStream<A>.filter(cond: (A) -> Boolean): FStream<A> = when (this) {
    is Empty -> FStream()
    is Cons -> lazyIf(
        cond = cond(head()),
        onTrue = { FStream(head) { tail().filter(cond) } },
        onFalse = { tail().filter(cond) }
    )
}

fun <A> FStream<A>.append(other: () -> FStream<A>): FStream<A> = when (this) {
    is Empty -> other()
    is Cons -> FStream(head, other)
}


fun main() {
    val fstream: FStream<Int> = FStream.of(1, 2, 3, 4, 5)
    println(fstream.toList())
    println(fstream.take(4).toList())
    println(fstream.drop(2).toList())
    println(fstream.dropWhile { it % 2 == 1 }.toList())
    println(fstream.takeWhile { it < 4 }.toList())
    println(fstream.exist { it == 2 })
    println(fstream.forAll { it == 3 })
    println(fstream.map { it * 2 }.toList())
    println(fstream.filter { it % 2 == 1 }.toList())
}