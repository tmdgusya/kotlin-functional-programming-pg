package chapter3

fun <A> empty(): List<A> = Nil

// Example 3.14
fun <A> concat(doubleList: List<List<A>>): List<A> = foldRight(
    doubleList,
    Nil as List<A>
) { outer, inner -> foldRight(outer, inner) { h, l -> Cons(h, l) } }

// Example 3.15
fun addOne(xs: List<Int>) = foldRight<Int, List<Int>>(
    list = xs,
    defaultValue = empty(),
    f = { a, b -> Cons(a + 1, b) }
)

// Example 3.16
fun doubleToString(xs: List<Double>) = foldRight<Double, List<String>>(
    list = xs,
    defaultValue = empty(),
    f = { a, b -> Cons(a.toString(), b) }
)

// Example 3.17
fun <A, TO_BE> map(
    list: List<A>,
    f: (A) -> TO_BE,
) = foldRight<A, List<TO_BE>>(
    list = list,
    defaultValue = empty()
) { a, b -> Cons(f(a), b) }

// Example 3.19
fun <A, TO_BE> flatMap(
    list: List<A>,
    f: (A) -> List<TO_BE>,
) = foldRight<A, List<TO_BE>>(
    list = list,
    defaultValue = empty()
) { a, b -> f(a).append2(b) }

// Example 3.20
fun <A> List<A>.filter2(f: (A) -> Boolean): List<A> = flatMap(
    list = this,
) { a -> if(f(a)) List.of(a) else empty() }

// Example 3.21
fun List<Int>.add(other: List<Int>): List<Int> = zipWith(other) { a, b -> a + b }

// Example 3.22
fun <A> List<A>.zipWith(other: List<A>, f: (A, A) -> A): List<A> = when(this) {
    is Nil -> Nil
    is Cons -> when(other) {
        is Nil -> Nil
        is Cons -> Cons(f(this.head, other.head), this.tail.zipWith(other.tail, f))
    }
}

fun main() {
    val list1 = Cons(1, Cons(2, Cons(3, Cons(4, Nil))))
    val list2 = Cons(1, Cons(2, Cons(3, Cons(4, Nil))))
    println("Result : " + list1.add(list2))
    println(addOne(list1))
}