
import kotlinx.collections.immutable.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals

object Day1Spec: Spek({

    given("AoC 18.1.1") {

        it("works with task data") {
            assertEquals(585, Day1.solve_pt1(readLines("1.txt").map { it.toInt() }))
        }
    }

    given("AoC 18.1.2") {

        it("works with ref data 1") {
            assertEquals(0, Day1.solve_pt2_stream(listOf(+1, -1)))
        }

        it("works with ref data 2") {
            assertEquals(10, Day1.solve_pt2_stream(listOf(+3, +3, +4, -2, -4)))
        }

        it("works with ref data 3") {
            assertEquals(5, Day1.solve_pt2_stream(listOf(-6, +3, +8, +5, -6)))
        }

        it("works with ref data 4") {
            assertEquals(14, Day1.solve_pt2_stream(listOf(+7, +7, -2, -7, -4)))
        }


        benchedIt("works with task data", 500) {
            assertEquals(83173, Day1.solve_pt2_set(readLines("1.txt").map { it.toInt() }, 0, immutableHashSetOf(0), 0))
        }

        benchedIt("works with task data and mutable state", 500) {
            assertEquals(83173, Day1.solve_pt2_map(readLines("1.txt").map { it.toInt() }, 0, immutableHashMapOf(0 to 1), 0))
        }


        benchedIt("works with task data and streams", 500) {
            assertEquals(83173, Day1.solve_pt2_stream(readLines("1.txt").map { it.toInt() }))
        }

    }
})