package fold_practice

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class FListTest : FunSpec({

    test("tail") {
        val list = FList.of(1, 2, 3, 4)

        list.tail() shouldBe FList.of(2, 3, 4)
    }

    test("setHead") {
        val list = FList.of(1, 2, 3, 4)

        val changed = list.setHead(5)

        changed shouldBe FList.of(5, 1, 2, 3, 4)
    }

    test("drop") {
        val list = FList.of(1, 2, 3, 4)

        val dropped = list.drop(2)

        dropped shouldBe FList.of(3, 4)
    }

    test("dropWhile") {
        val list = FList.of(1, 2, 3, 4, 5)

        val dropped = list.dropWhile { it < 3 }

        dropped shouldBe FList.of(3, 4, 5)
    }

    test("append test") {
        val list1 = FList.of(1, 2, 3, 4)
        val list2 = FList.of(5, 6, 7, 8)

        val merged = list1 + list2

        merged shouldBe FList.of(1, 2, 3, 4, 5, 6, 7, 8)
    }

    test("init") {
        val list1 = FList.of(1, 2, 3, 4)

        list1.init() shouldBe FList.of(1, 2, 3)
    }
})
