package chapter3

sealed class List<out A> {
    companion object {
        fun <A> of(vararg aa: A): List<A> {
            val tail = aa.sliceArray(1 until aa.size)
            return if (aa.isEmpty()) Nil else Cons(aa[0], of(*tail))
        }
    }
}

object Nil : List<Nothing>() {
    override fun toString(): String = "Nil"
}

data class Cons<out A>(val head: A, val tail: List<A>): List<A>()

// Example 3.1
fun <A> List<A>.tail(): List<A> = when (this) {
    is Nil -> Nil
    is Cons<A> -> this.tail
}

// Example 3.2
fun <A> List<A>.setHead(x: A): List<A> = when (this) {
    is Nil -> throw UnsupportedOperationException("This operation is not supported for Nil")
    is Cons<A> -> Cons(x, this.tail)
}

// Example 3.3
tailrec fun <A> List<A>.drop(n: Int): List<A> = if (n == 0) this else this.tail().drop(n - 1)

// Example 3.4
tailrec fun <A> List<A>.dropWhile(f: (A) -> Boolean): List<A> = when (this) {
    is Nil -> Nil
    is Cons -> if (f(this.head)) this.drop(1).dropWhile(f) else this
}

// Example 3.5
fun <A> List<A>.init(): List<A> = when (this) {
    is Nil -> Nil
    is Cons<A> -> if (this.tail is Nil) Nil else Cons(this.head, this.tail.init())
}

fun <A> List<A>.append(other: List<A>): List<A> = when (this) {
    is Nil -> other
    is Cons -> Cons(head = this.head, tail = this.tail.append(other))
}

fun main() {
    val list = Cons(1, Cons(3, Cons(4, Cons(5, Nil))))
    val test = list.init()
    println(test)
}