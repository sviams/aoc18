
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals

object Day16Spec: Spek({

    given("AoC 18.16.1") {

        it("works with ref data") {
            assertEquals(1, Day16.solve_pt1(readLines("16_example.txt")))
        }

        it("works with task data") {
            assertEquals(500, Day16.solve_pt1(readLines("16.txt")))
        }
    }

    given("AoC 18.16.2") {

        it("works with task data") {
            assertEquals(533, Day16.solve_pt2(readLines("16.txt"), readLines("16_2.txt")))
        }
    }

})
