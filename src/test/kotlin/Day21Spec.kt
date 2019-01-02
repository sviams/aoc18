
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals

object Day21Spec: Spek({

    given("AoC 18.21.1") {

        it("works with task data") {
            assertEquals(2792537, Day21.solve_pt1(readLines("21.txt")))
        }
    }

    given("AoC 18.21.2") {

        it("works with task data") {
            assertEquals(10721810, Day21.solve_pt2(readLines("21.txt")))
        } // 5 min runtime :(
    }

})
