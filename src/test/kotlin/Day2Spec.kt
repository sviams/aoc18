
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals

object Day2Spec: Spek({

    given("AoC 18.2.1") {

        it("works with ref data") {
            assertEquals(12, Day2.solve_pt1(listOf("abcdef", "bababc", "abbcde", "abcccd", "aabcdd", "abcdee", "ababab")))
        }

        it("works with task data") {
            assertEquals(5704, Day2.solve_pt1(readLines("2.txt")))
        }
    }

    given("AoC 18.2.2") {

        it("works with ref data") {
            assertEquals("fgij", Day2.solve_pt2(listOf("abcde", "fghij", "klmno", "pqrst", "fguij", "axcye", "wvxyz")))
        }

        it("works with task data") {
            assertEquals("umdryabviapkozistwcnihjqx", Day2.solve_pt2(readLines("2.txt")))
        }

    }

})
