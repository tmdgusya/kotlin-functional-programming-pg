package chapter05

fun <A> lazyIf(
    cond: Boolean,
    onTrue: () -> A,
    onFalse: () -> A
): A = if (cond) onTrue() else onFalse()

fun main() {
    val a = 30
    val y: Unit = lazyIf(
        cond = (a < 22),
        onTrue = { println("a") },
        onFalse = { println("b") }
    )
    println(y) // The answer is expected to print out "b"
}