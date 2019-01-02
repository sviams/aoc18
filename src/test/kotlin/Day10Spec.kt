
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals

object Day10Spec: Spek({

    given("AoC 18.10.1") {

        it("works with ref data") {
            assertEquals(-1, Day10.solve_pt1(readLines("10_example.txt")))
        }

        it("works with task data") {
            assertEquals(-1, Day10.solve_pt1(readLines("10.txt")))
        }
    }

    given("AoC 18.10.2") {

        it("works with ref data") {
            assertEquals(3, Day10.solve_pt2(readLines("10_example.txt")))
        }

        it("works with task data") {
            assertEquals(10101, Day10.solve_pt2(readLines("10.txt")))
        }

    }

})
