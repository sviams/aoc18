
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals

object Day14Spec: Spek({

    given("AoC 18.14.1") {

        it("works with ref data 1") {
            assertEquals("5158916779", Day14.solve_pt1(9))
        }

        it("works with ref data 2") {
            assertEquals("0124515891", Day14.solve_pt1(5))
        }

        it("works with ref data 3") {
            assertEquals("9251071085", Day14.solve_pt1(18))
        }

        it("works with ref data 4") {
            assertEquals("5941429882", Day14.solve_pt1(2018))
        }

        it("works with task data") {
            assertEquals("3147574107", Day14.solve_pt1(293801))
        }
    }

    given("AoC 18.14.2") {

        it("works with ref data 1") {
            assertEquals(9, Day14.solve_pt2("51589"))
        }

        it("works with ref data 2") {
            assertEquals(5, Day14.solve_pt2("01245"))
        }

        it("works with ref data 3") {
            assertEquals(18, Day14.solve_pt2("92510"))
        }

        it("works with ref data 4") {
            assertEquals(2018, Day14.solve_pt2("59414"))
        }

        it("works with task data") {
            assertEquals(20280190, Day14.solve_pt2("293801"))
        }

    }

})
