
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals

object Day13Spec: Spek({

    given("AoC 18.13.1") {

        it("works with ref data") {
            assertEquals("7,3", Day13.solve_pt1(readLines("13_example.txt")))
        }

        it("works with task data") {
            assertEquals("8,9", Day13.solve_pt1(readLines("13.txt")))
        }
    }

    given("AoC 18.13.2") {

        it("works with ref data") {
            assertEquals("6,4", Day13.solve_pt2(readLines("13_example2.txt")))
        }

        it("works with task data") {
            assertEquals("73,33", Day13.solve_pt2(readLines("13.txt")))
        }

    }

})
