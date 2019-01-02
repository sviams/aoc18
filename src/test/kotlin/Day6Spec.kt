
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals

object Day6Spec: Spek({

    given("AoC 18.6.1") {

        it("works with ref data") {
            assertEquals(12, Day6.solve_pt1(readLines("6_example.txt")))
        }

        it("works with task data") {
            assertEquals(4589, Day6.solve_pt1(readLines("6.txt")))
        }
    }

    given("AoC 18.6.2") {

        it("works with ref data") {
            assertEquals(16, Day6.solve_pt2(readLines("6_example.txt"), 32))
        }

        it("works with task data") {
            assertEquals(40252, Day6.solve_pt2(readLines("6.txt"), 10000))
        }

    }

})
