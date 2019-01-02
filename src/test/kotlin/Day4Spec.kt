
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals

object Day4Spec: Spek({

    given("AoC 18.4.1") {

        it("works with ref data") {
            assertEquals(240, Day4.solve_pt1(readLines("4_example.txt")))
        }

        it("works with task data") {
            assertEquals(87681, Day4.solve_pt1(readLines("4.txt")))
        }
    }

    given("AoC 18.4.2") {

        it("works with ref data") {
            assertEquals(4455, Day4.solve_pt2(readLines("4_example.txt")))
        }

        it("works with task data") {
            assertEquals(136461, Day4.solve_pt2(readLines("4.txt")))
        }

    }

})
