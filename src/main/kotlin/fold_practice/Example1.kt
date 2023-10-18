package fold_practice

import chapter04.Option
import chapter3.Cons
import fold_practice.FList.*


sealed class FList<out A> {
    data object Nil : FList<Nothing>()
    data class FCons<out A>(val head: A, val tail: FList<A>) : FList<A>()

    companion object {
        fun <A> of(vararg elements: A): FList<A> {
            val tail = elements.sliceArray(1..<elements.size)
            return if (elements.isEmpty()) Nil else FCons(elements[0], FList.of(*tail))
        }
        operator fun <A> invoke(a: A, b: FList<A>): FList<A> = FCons(a, b)
        operator fun <A> invoke(): FList<A> = Nil
    }

    fun tail(): FList<A> = when (this) {
        is Nil -> Nil
        is FCons<A> -> this.tail
    }

    fun <A> setHead(x: A): FList<A> = when (this) {
        is Nil -> FCons(x, Nil)
        is FCons -> FCons(x, this as FList<A>)
    }

    fun drop(n: Int): FList<A> = when (this) {
        is Nil -> Nil
        is FCons -> if (n == 0) this else tail.drop(n - 1)
    }

    fun dropWhile(condition: (A) -> Boolean): FList<A> = when (this) {
        is Nil -> Nil
        is FCons -> if (condition(head)) drop(1).dropWhile(condition) else this
    }

    fun <A, B> FList<A>.foldRight(acc: B, map: (A, B) -> B): B = when (this) {
        is Nil -> acc
        is FCons -> map(head, tail.foldRight(acc, map))
    }
}

operator fun <A> FList<A>.plus(other: FList<A>): FList<A> = when (this) {
    is Nil -> other
    is FCons -> FCons(head, tail + other)
}

fun <A> FList<A>.append1(other: FList<A>): FList<A> = when (this) {
    is Nil -> other
    is FCons -> FCons(head, tail.append1(other))
}

private tailrec fun <A> FList<A>._init(acc: FList<A>): FList<A> = when (this) {
    is Nil -> this
    is FCons<A> -> if (this.tail == Nil) acc else tail._init(acc + FCons(head, Nil))
}

fun <A> FList<A>.init(): FList<A> = when (this) {
    is Nil -> Nil
    is FCons -> tail._init(FCons(head, Nil as FList<A>))
}

tailrec fun <A, B> FList<A>.foldLeft(acc: B, map: (B, A) -> B): B = when (this) {
    is Nil -> acc
    is FCons -> tail.foldLeft(map(acc, head), map)
}