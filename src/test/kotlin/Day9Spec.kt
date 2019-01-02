
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals

object Day9Spec: Spek({

    given("AoC 18.9.1") {

        it("works with ref data 1") {
            assertEquals(32, Day9.solve(9,25))
        }

        it("works with ref data 2") {
            assertEquals(8317, Day9.solve(10,1618))
        }

        it("works with ref data 3") {
            assertEquals(2764, Day9.solve(17,1104))
        }

        it("works with task data") {
            assertEquals(429287, Day9.solve(410, 72059))
        }
    }

    given("AoC 18.9.2") {

        it("works with task data") {
            assertEquals(3624387659, Day9.solve(410, 72059 * 100))
        }

    }

})
