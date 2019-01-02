
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals

object Day18Spec: Spek({

    given("AoC 18.18.1") {

        it("works with ref data") {
            assertEquals(1147, Day18.solve(readLines("18_example.txt"), 10))
        }

        it("works with task data") {
            assertEquals(514944, Day18.solve(readLines("18.txt"), 10))
        }
    }

    given("AoC 18.18.2") {

        it("works with test cutoff") {
            assertEquals(193050, Day18.solve(readLines("18.txt"), 1000))
        }

        it("works with task data") {
            assertEquals(193050, Day18.solve(readLines("18.txt"), 1000000000))
        }
    }
})
