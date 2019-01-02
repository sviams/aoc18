
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals

object Day8Spec: Spek({

    given("AoC 18.8.1") {

        it("works with ref data") {
            assertEquals(138, Day8.solve_pt1("2 3 0 3 10 11 12 1 1 0 1 99 2 1 1 2"))
        }

        it("works with task data") {
            assertEquals(36027, Day8.solve_pt1(readLine("8.txt")))
        }
    }

    given("AoC 18.8.2") {

        it("works with ref data") {
            assertEquals(66, Day8.solve_pt2("2 3 0 3 10 11 12 1 1 0 1 99 2 1 1 2"))
        }

        it("works with task data") {
            assertEquals(23960, Day8.solve_pt2(readLine("8.txt")))
        }

    }

})
