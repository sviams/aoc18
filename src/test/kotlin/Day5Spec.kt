
import kotlinx.collections.immutable.toImmutableList
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals

object Day5Spec: Spek({

    given("AoC 18.5.1") {

        it("works with ref data") {
            assertEquals(10, Day5.solve_pt1("dabAcCaCBAcCcaDA"))
        }

        it("works with task data") {
            assertEquals(10804, Day5.solve_pt1(readLine("5.txt")))
        }
    }

    given("AoC 18.5.2") {

        it("works with task data") {
            assertEquals(6650, Day5.solve_pt2(readLine("5.txt")))
        }

    }

})
