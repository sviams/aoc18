
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals

object Day25Spec: Spek({

    given("AoC 18.25.1") {

        it("works with ref data") {
            assertEquals(2, Day25.solve_pt1(readLines("25_example.txt")))
        }

        it("works with ref data 2") {
            assertEquals(4, Day25.solve_pt1(readLines("25_example2.txt")))
        }

        it("works with ref data 3") {
            assertEquals(3, Day25.solve_pt1(readLines("25_example3.txt")))
        }

        it("works with ref data 4") {
            assertEquals(8, Day25.solve_pt1(readLines("25_example4.txt")))
        }

        it("works with task data") {
            assertEquals(386, Day25.solve_pt1(readLines("25.txt")))
        }
    }

})
