package chapter04

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlin.math.pow

class Example1KtTest : FunSpec({
    test("variance") {
        fun mean(xs: List<Double>): Option<Double> = if (xs.isEmpty()) Option.invoke() else Option(xs.sum() / xs.size)
        fun variance_book(xs: List<Double>) = mean(xs).flatMap { m ->
            mean(xs.map { x -> (x - m).pow(2) })
        }

        val list = listOf(0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.85, 0.99)

        variance_book(list) shouldBe variance(list)
    }

    test("map2") {
        val result = Option(1).map2(Option(2)) { a, b -> a + b }

        result.getOrElse { 0 } shouldBe 3
    }

    test("sequence") {
        val result: List<Option<Int>> = listOf(
            Option(1),
            Option.None
        )

        val result1: List<Option<Int>> = listOf(
            Option(1),
            Option(2),
        )

        sequence(result) shouldBe Option.None
        sequence(result1) shouldBe Option(listOf(1,2))
    }
})
