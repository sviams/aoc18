
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals

object Day17Spec: Spek({

    given("AoC 18.17.1") {

        it("works with ref data") {
            assertEquals(57, Day17.solve_pt1(readLines("17_example.txt")))
        }

        it("works with task data") {
            assertEquals(38409, Day17.solve_pt1(readLines("17.txt")))
        }
    }

    given("AoC 18.17.2") {

        it("works with ref data") {
            assertEquals(29, Day17.solve_pt2(readLines("17_example.txt")))
        }

        it("works with task data") {
            assertEquals(32288, Day17.solve_pt2(readLines("17.txt")))
        }
    }
})
