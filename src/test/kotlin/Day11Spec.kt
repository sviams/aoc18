
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals

object Day11Spec: Spek({

    given("AoC 18.11.1") {

        it("works with fuel cell ref data 1") {
            assertEquals(4, Day11.powerLevel(3,5, 8))
        }

        it("works with fuel cell ref data 2") {
            assertEquals(0, Day11.powerLevel(217,196, 39))
        }

        it("works with fuel cell ref data 3") {
            assertEquals(-5, Day11.powerLevel(122,79, 57))
        }

        it("works with fuel cell ref data 4") {
            assertEquals(4, Day11.powerLevel(101,153, 71))
        }

        it("works with ref data") {
            assertEquals(Day11.Result(21,61, 3), Day11.solve_pt1(42))
        }

        it("works with task data") {
            assertEquals(Day11.Result(21,68, 3), Day11.solve_pt1(2568))
        }
    }

    given("AoC 18.11.2") {

        it("works with ref data") {
            assertEquals(Day11.Result(90,269, 16), Day11.solve_pt2(18))
        }

        it("works with task data") {
            assertEquals(Day11.Result(90,201, 15), Day11.solve_pt2(2568))
        }

    }

})
