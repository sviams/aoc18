
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals

object Day7Spec: Spek({

    given("AoC 18.7.1") {

        it("works with ref data") {
            assertEquals("CABDFE", Day7.solve_pt1(readLines("7_example.txt")))
        }

        it("works with task data") {
            assertEquals("ABLCFNSXZPRHVEGUYKDIMQTWJO", Day7.solve_pt1(readLines("7.txt")))
        }
    }

    given("AoC 18.7.2") {

        it("works with ref data") {
            assertEquals(15, Day7.solve_pt2(readLines("7_example.txt"), 2, 0))
        }

        it("works with task data") {
            assertEquals(1157, Day7.solve_pt2(readLines("7.txt"), 5, 60))
        }

    }

})
