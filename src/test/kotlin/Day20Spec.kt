
import kotlinx.collections.immutable.toImmutableList
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals

object Day20Spec: Spek({

    given("AoC 18.20.1") {

        it("works with ref data") {
            assertEquals(3, Day20.solve_pt1("^WNE\$"))
        }

        it("works with ref data 2") {
            assertEquals(10, Day20.solve_pt1("^ENWWW(NEEE|SSE(EE|N))\$"))
        }

        it("works with ref data 3") {
            assertEquals(18, Day20.solve_pt1("^ENNWSWW(NEWS|)SSSEEN(WNSE|)EE(SWEN|)NNN\$"))
        }

        it("works with task data") {
            assertEquals(3725, Day20.solve_pt1(readLine("20.txt")))
        }
    }

    given("AoC 18.20.2") {

        it("works with task data") {
            assertEquals(8541, Day20.solve_pt2(readLine("20.txt")))
        }
    }

})
