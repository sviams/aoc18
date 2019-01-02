
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals

object Day15Spec: Spek({

    given("AoC 18.15.1") {

        given("ref data") {

            it("works with ref data") {
                assertEquals(27730, Day15.solve_pt1(readLines("15_example.txt")))
            } // Gobs

            it("works with ref data 2") {
                assertEquals(36334, Day15.solve_pt1(readLines("15_example2.txt")))
            } // Elves

            it("works with ref data 3") {
                assertEquals(39514, Day15.solve_pt1(readLines("15_example3.txt")))
            } // Elves

            it("works with ref data 4") {
                assertEquals(27755, Day15.solve_pt1(readLines("15_example4.txt")))
            } // Gobs

            it("works with ref data 5") {
                assertEquals(28944, Day15.solve_pt1(readLines("15_example5.txt")))
            } // Gobs

            it("works with ref data 6") {
                assertEquals(18740, Day15.solve_pt1(readLines("15_example6.txt")))
            } // Gobs

            it("works with ref data 7") {
                assertEquals(146, Day15.solve_pt1(readLines("15_example7.txt")))
            } // Gobs

        }

        it("works with Pers task data") {
            assertEquals(207542, Day15.solve_pt1(readLines("15_per.txt")))
        }

        it("works with my task data") {
            assertEquals(206236, Day15.solve_pt1(readLines("15.txt")))
        }

    }

    given("AoC 18.15.2") {

        it("works with ref data") {
            assertEquals(4988, Day15.solve_pt2(readLines("15_example.txt")))
        }

        it("works with task data") {
            assertEquals(88537, Day15.solve_pt2(readLines("15.txt")))
        }

    }

})
