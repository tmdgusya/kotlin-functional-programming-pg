package chapter3

sealed class Tree<out A>

data class Leaf<A>(val value: A): Tree<A>()

data class Branch<A>(val left: Tree<A>, val right: Tree<A>): Tree<A>()

// Example 3.24
fun <A> Tree<A>.size(): Int = when (this) {
    is Leaf -> 1
    is Branch -> 1 + this.left.size() + this.right.size()
}

// Example 3.25
fun Tree<Int>.maximum(): Int = when (this) {
    is Leaf -> value
    is Branch -> left.maxOf(right)
}

fun Tree<Int>.maxOf(other: Tree<Int>): Int = when (this@maxOf) {
    is Leaf -> when (other) {
        is Leaf -> maxOf(this@maxOf.value, other.value)
        is Branch -> maxOf(
            other.left.maxOf(this@maxOf),
            other.right.maxOf(this@maxOf)
        )
    }
    is Branch -> maxOf(
        this@maxOf.left.maxOf(other),
        this@maxOf.right.maxOf(other)
    )
}

// Example 3.26
fun <A> Tree<A>.depth(): Int = when (this) {
    is Leaf -> 1
    is Branch -> maxOf(this.left.size(), this.right.size())
}

// Example 3.27
fun <A, B> Tree<A>.map(f: (A) -> B): Tree<B> = when(this) {
    is Leaf -> Leaf(f(value))
    is Branch -> Branch(
        left = left.map(f),
        right = right.map(f),
    )
}

// Example 3.28
fun <A, B> Tree<A>.fold(f: (A) -> B, b: (B, B) -> B): B = when (this) {
    is Leaf -> f(value)
    is Branch -> b(left.fold(f, b), right.fold(f, b))
}

fun <A> Tree<A>.sizeF(): Int = fold(
    f = { 1 }
) { acc1, acc2 -> 1 + acc1 + acc2 }

fun Tree<Int>.maximumF(): Int = fold(
    f = { a -> a }
) { a, b -> maxOf(a, b) }

fun <A> Tree<A>.depthF(): Int = fold(
    f = { _ -> 1 }
) { a, b -> 1 + maxOf(a, b) }

fun <A, B> Tree<A>.mapF(f: (A) -> B): Tree<B> = fold(f = { Leaf(f(it)) } ) { lb: Tree<B>, rb: Tree<B> -> Branch(lb, rb) }



fun main() {
    val tree = Branch(Branch(Leaf(1), Leaf(2)), Leaf(6))
    println(tree.sizeF())
    println(tree.maximumF())
    println(tree.depth())
    println(tree.map { i -> i + 1 })
}
