import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.immutableListOf
import kotlinx.collections.immutable.toImmutableList

object Day18 {

    fun parseInput(input: List<String>) : ImmutableList<ImmutableList<Char>> =
        input.fold(immutableListOf()) { acc, line ->
            val charList: ImmutableList<Char> = line.toCharArray().toList().toImmutableList()
            acc.add(charList)
        }

    private fun findAdjacents(
        state: ImmutableList<ImmutableList<Char>>,
        rowIndex: Int,
        colIndex: Int
    ): ImmutableList<Char> {
        val initial: Char = state[rowIndex][colIndex]
        return (-1 .. 1).flatMap { r ->
            val row = state.getOrNull(rowIndex + r)
            (-1 .. 1).map { c ->
                row?.getOrNull(colIndex + c)
            }
        }.filterNotNull().minus(initial).toImmutableList()
    }

    tailrec fun evolve(state: ImmutableList<ImmutableList<Char>>, history: ImmutableList<ImmutableList<ImmutableList<Char>>>, age: Long, cutoff: Long) : ImmutableList<ImmutableList<Char>> {
        if (age == cutoff) return state
        val newState = state.mapIndexed { rowIndex, row ->
            row.mapIndexed { colIndex, col ->
                val adjacents = findAdjacents(state, rowIndex, colIndex)
                when (col) {
                    '.' -> if (adjacents.count { it == '|' } >= 3) '|' else '.'
                    '|' -> if (adjacents.count { it == '#' } >= 3) '#' else '|'
                    else -> if (adjacents.count { it == '|' } >= 1 && adjacents.count { it == '#' } >= 1) '#' else '.'
                }
            }.toImmutableList()
        }.toImmutableList()

        return if (history.contains(newState) && age < 100000) {
            val prev = history.lastIndexOf(newState)
            val cycle = age - prev
            val remainingAfterSkip = (cutoff - age) % cycle
            val skipToAge = cutoff - remainingAfterSkip
            evolve(newState, immutableListOf(), skipToAge + 1, cutoff)
        } else evolve(newState, history.add(newState), age + 1, cutoff)
    }

    fun solve(input: List<String>, cutoff: Long) : Int {
        val start = parseInput(input)
        val end = evolve(start, immutableListOf(), 0, cutoff)
        val woods = end.sumBy { it.count { c -> c == '|' } }
        val lumberYards = end.sumBy { it.count { c -> c == '#' } }
        return woods * lumberYards
    }
}