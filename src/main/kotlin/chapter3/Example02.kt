package chapter3

tailrec fun <TYPE, RETURN_TYPE> foldRight(
    list: List<TYPE>,
    defaultValue: RETURN_TYPE,
    f: (TYPE, RETURN_TYPE) -> RETURN_TYPE
): RETURN_TYPE = when (list) {
    is Nil -> defaultValue
    is Cons -> foldRight(list.tail, f(list.head, defaultValue), f)
}

// Example 3.7
// Cons(head=1, tail=Cons(head=2, tail=Cons(head=3, tail=Nil)))
// 서로 같으므로 치환가능 한 존재?

// Example 3.8
fun <A> length(xs: List<A>) = foldRight(
    list = xs,
    defaultValue = 0,
    f = { a, b -> b + 1 },
)

// Example 3.9
tailrec fun <TYPE, RETURN_TYPE> foldLeft(
    list: List<TYPE>,
    defaultValue: RETURN_TYPE,
    f: (RETURN_TYPE, TYPE) -> RETURN_TYPE
): RETURN_TYPE = when (list) {
    is Nil -> defaultValue
    is Cons -> foldLeft(list.tail, f(defaultValue, list.head), f)
}

// Example 3.10
fun sum(xs: List<Int>) = foldLeft(
    list = xs,
    defaultValue = 0,
    f = { a, b -> b + a }
)

fun product(abs: List<Double>) = foldLeft(
    list = abs,
    defaultValue = 1.0,
    f = { a , b -> b * a }
)

fun <A> length_(xs: List<A>) = foldRight(
    list = xs,
    defaultValue = 0,
    f = { a, b -> b + 1 },
)

// Example 3.11
fun <A> reverse(list: List<A>) = foldLeft(
    list = list,
    defaultValue = Nil as List<A>,
    f = { a, b -> Cons(b, a) }
)

// Example 3.12
fun <TYPE, RETURN_TYPE> foldLeftR(
    list: List<TYPE>,
    defaultValue: RETURN_TYPE,
    f: (RETURN_TYPE, TYPE) -> RETURN_TYPE
): RETURN_TYPE = foldRight(
    list,
    { returnType -> returnType },
    { a: TYPE, g: (RETURN_TYPE) -> RETURN_TYPE -> { b: RETURN_TYPE -> g(f(b,a))} }
)(defaultValue)

// Example 3.13
fun <A> List<A>.append2(other: List<A>): List<A> = foldRight(
    list = this,
    defaultValue = other,
    f = { a, r -> Cons(a, r) }
)

fun main() {
    val list1 = Cons(1.0, Cons(2, Cons(3, Cons(4, Nil))))
    val list2 = Cons(1.0, Cons(2, Cons(3, Cons(4, Nil))))
    val test = length(list1)
    val test2 = list1.append2(list2)
    println(test2)
}