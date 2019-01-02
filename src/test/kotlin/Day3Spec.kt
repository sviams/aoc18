
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals

object Day3Spec: Spek({

    given("AoC 18.3.1") {

        it("works with ref data") {
            assertEquals(4, Day3.solve_pt1(listOf("#1 @ 1,3: 4x4", "#2 @ 3,1: 4x4", "#3 @ 5,5: 2x2")))
        }

        it("works with task data") {
            assertEquals(116140, Day3.solve_pt1(readLines("3.txt")))
        }
    }

    given("AoC 18.3.2") {

        it("works with ref data") {
            assertEquals(3, Day3.solve_pt2(listOf("#1 @ 1,3: 4x4", "#2 @ 3,1: 4x4", "#3 @ 5,5: 2x2")))
        }

        it("works with task data") {
            assertEquals(574, Day3.solve_pt2(readLines("3.txt")))
        }

    }

})
