import kotlinx.collections.immutable.*

object Day1 {

    fun solve_pt1(input: List<Int>) : Int = input.sum()

    // ~71ms
    tailrec fun solve_pt2_set(input: List<Int>, index: Int, visited: ImmutableSet<Int>, last: Int) : Int {
        val next = last + input[index]
        return if (visited.contains(next)) next
        else solve_pt2_set(input, (index+1) % input.size, visited.plus(next), next)
    }

    // ~61ms
    tailrec fun solve_pt2_map(input: List<Int>, index: Int, visited: ImmutableMap<Int, Int>, last: Int) : Int {
        val next = last + input[index]
        return if (visited.contains(next)) next
        else solve_pt2_map(input, (index+1) % input.size, visited.plus(next to 1), next)
    }

    data class State(val visited: ImmutableSet<Int>, val index: Int, val next: Int)

    // ~75ms
    fun solve_pt2_stream(input: List<Int>) : Int =
        generateSequence(State(immutableHashSetOf(0), 0, input[0])) { (visited, index, last) ->
            val nextIndex = (index+1) % input.size
            State(visited.plus(last), nextIndex, last + input[nextIndex])
        }.takeWhileInclusive { !it.visited.contains(it.next) }.last().next
}