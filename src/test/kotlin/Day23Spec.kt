
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals

object Day23Spec: Spek({

    given("AoC 18.23.1") {

        it("works with ref data") {
            assertEquals(7, Day23.solve_pt1(readLines("23_example.txt")))
        }

        it("works with task data") {
            assertEquals(713, Day23.solve_pt1(readLines("23.txt")))
        }
    }

    given("AoC 18.23.2") {

        it("works with ref data") {
            assertEquals(34, Day23.solve_pt2(readLines("23_example2.txt")))
        }

        it("works with Pers data") {
            assertEquals(56930129, Day23.solve_pt2(readLines("23_per.txt")))
        } // Wrong answer for this input

        it("works with task data") {
            assertEquals(104501042, Day23.solve_pt2(readLines("23.txt")))
        } // Correct answer for my input, lucky me...
    }

})
