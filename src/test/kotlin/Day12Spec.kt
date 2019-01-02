
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals

object Day12Spec: Spek({

    given("AoC 18.12.1") {

        it("works with ref data") {
            assertEquals(325, Day12.solve_pt1(readLines("12_example.txt")))
        }

        it("works with task data") {
            assertEquals(4818, Day12.solve_pt1(readLines("12.txt")))
        }
    }

    given("AoC 18.12.2") {

        it("works with task data") {
            assertEquals(5100000001377, Day12.solve_pt2(readLines("12.txt")))
        }

    }

})
