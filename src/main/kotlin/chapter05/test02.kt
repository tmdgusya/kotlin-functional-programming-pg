package chapter05

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

// the value of i is always evaluated when it is needed
fun maybeTwice(b: Boolean, i: () -> Int) = if(b) i() + i() else 0

// the value of i is evaluated only one time when it is needed at first time
@ExperimentalContracts
fun maybeTwiceCached(b: Boolean, thunk: () -> Int): Int {
    contract {
       callsInPlace(thunk, InvocationKind.EXACTLY_ONCE)
    }
    val j: Int by lazy(thunk)
    return if(b) j + j else 0
}

@OptIn(ExperimentalContracts::class)
fun main() {
    val x = maybeTwiceCached(true) { println("hi"); 1 + 41 }
    println(x)
}