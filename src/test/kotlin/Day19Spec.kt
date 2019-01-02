
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals

object Day19Spec: Spek({

    given("AoC 18.19.1") {

        it("works with ref data") {
            assertEquals(6, Day19.solve_pt1(readLines("19_example.txt")))
        }

        it("works with task data") {
            assertEquals(978, Day19.solve_pt1(readLines("19.txt")))
        }
    }

    given("AoC 18.19.2") {

        it("works with task data") {
            assertEquals(10996992, Day19.solve_pt2(readLines("19.txt")))
        }
    }

})
