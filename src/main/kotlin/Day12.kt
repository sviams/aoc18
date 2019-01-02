import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableHashMap

object Day12 {

    data class State(val pots: ImmutableMap<Int, Int>, val rules: List<Pair<List<Int>, Int>>)

    fun parseInput(input: List<String>) : State {
        val init = input[0].substring(15).mapIndexed { index, c -> index to if (c == '#') 1 else 0 }.toMap().filterValues { it == 1 }.toImmutableHashMap()
        val transits = input.subList(2, input.size).map { line ->
            val parts = line.split(" => ")
            val pattern = parts[0].map { c -> if (c == '#') 1 else 0}
            val result = parts[1].map { c -> if (c == '#') 1 else 0 }
            pattern to result.single()
        }
        return State(init, transits)
    }

    fun incrementState(pots: ImmutableMap<Int, Int>, rules: List<Pair<List<Int>, Int>>) : ImmutableMap<Int, Int> {
        val begin = pots.keys.sorted().first()
        val end = pots.keys.sorted().last()
        return (begin-1 .. end+1).map { index ->
            val values = (index-2 .. index+2).map { pots[it] ?: 0 }
            val newVal = rules.singleOrNull { it.first == values }?.second ?: 0
            index to newVal
        }.toMap().filterValues { it == 1 }.toImmutableHashMap()
    }

    fun potPattern(pots: ImmutableMap<Int, Int>) : Pair<String, Long> {
        val begin = pots.keys.sorted().first()
        return (begin .. pots.keys.sorted().last()).map { index -> if ((pots[index] ?: 0) == 1) '#' else '.' }.joinToString("") to begin.toLong()
    }

    fun sumPattern(pattern: Pair<String, Long>, offset: Long) : Long =
        pattern.first.foldRightIndexed(0L) { index: Int, c: Char, acc: Long ->
            acc + if (c == '#') offset + pattern.second + index else 0
        }

    tailrec fun evolve(pots: ImmutableMap<Int, Int>, transits: List<Pair<List<Int>, Int>>, index: Long, end: Long) : Long {
        val newState = incrementState(pots, transits)
        val newPattern = potPattern(newState)
        return if (newPattern.first == potPattern(pots).first || index == end) sumPattern(newPattern, end - index)
        else evolve(newState, transits, index+1, end)
    }

    fun solve_pt1(input: List<String>) : Long {
        val initState = parseInput(input)
        return evolve(initState.pots, initState.rules, 1, 20)
    }

    fun solve_pt2(input: List<String>) : Long {
        val initState = parseInput(input)
        return evolve(initState.pots, initState.rules, 1, 50000000000)
    }
}