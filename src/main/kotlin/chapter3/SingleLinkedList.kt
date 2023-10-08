package chapter3

sealed class List<out A> {
    companion object {
        fun <A> of(vararg aa: A): List<A> {
            val tail = aa.sliceArray(1 until aa.size)
            return if (aa.isEmpty()) Nil else Cons(aa[0], of(*tail))
        }
    }
}

object Nil : List<Nothing>()

data class Cons<out A>(val head: A, val tail: List<A>): List<A>()

fun sum(ints: List<Int>): Int = when (ints) {
    is Nil -> 0
    is Cons -> ints.head + sum(ints.tail)
}

fun main() {
    val list1 = Cons("abc", Cons("def", Nil))
}