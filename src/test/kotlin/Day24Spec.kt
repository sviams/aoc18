
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals

object Day24Spec: Spek({

    given("AoC 18.24.1") {

        it("works with ref data") {
            assertEquals(5216, Day24.solve_pt1(readLines("24_example.txt")))
        }

        it("works with task data") {
            assertEquals(26343, Day24.solve_pt1(readLines("24.txt")))
        }
    }

    given("AoC 18.24.2") {

        it("works with ref data") {
            assertEquals(51, Day24.do_solve_pt2(readLines("24_example.txt"), 1570))
        }

        it("works with task data") {
            assertEquals(5549, Day24.solve_pt2(readLines("24.txt")))
        }
    }

})
