
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals

object Day22Spec: Spek({

    given("AoC 18.22.1") {

        it("works with ref data") {
            assertEquals(114, Day22.solve_pt1(510, Day22.Pos(10, 10  )))
        }

        it("works with task data") {
            assertEquals(6323, Day22.solve_pt1(3879, Day22.Pos(8, 713)))
        }
    }

    given("AoC 18.22.2") {

        it("works with ref data") {
            assertEquals(45, Day22.solve_pt2(510, Day22.Pos(10, 10  )))
        }

        it("works with task data") {
            assertEquals(982, Day22.solve_pt2(3879, Day22.Pos(8, 713)))
        }
    }
})
