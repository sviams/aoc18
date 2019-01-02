import kotlinx.collections.immutable.*

object Day20 {

    data class Pos(val col: Int, val row: Int) {
        fun plus(other: Pos) = Pos(col + other.col, row + other.row)
    }

    val dirMap = immutableHashMapOf('N' to Pos(0,-1), 'E' to Pos(1, 0), 'S' to Pos(0,1), 'W' to Pos(-1, 0))

    tailrec fun walk(ahead: ImmutableList<Char>, grid: ImmutableMap<Pos, Int>, stack: ImmutableList<Pair<Pos, Int>>, dist: Int, currentPos: Pos) : ImmutableMap<Pos, Int> {
        if (ahead.isEmpty()) return grid
        val nextChar = ahead.first()
        val remaining = ahead.drop(1).toImmutableList()
        return when (nextChar) {
            '(' -> walk(remaining, grid, stack.add(currentPos to dist), dist, currentPos)
            '|' -> {
                val (pos, d) = stack.last()
                walk(remaining, grid, stack, d, pos)
            }
            ')' -> {
                val (pos, d) = stack.last()
                walk(remaining, grid, stack.dropLast(1).toImmutableList(), d, pos)
            }
            else -> {
                val asd: Pos = dirMap[nextChar]!!
                val newPos = currentPos.plus(asd)
                val newDist = dist + 1
                val newGrid = if (!grid.containsKey(newPos) || grid[newPos]!! > newDist) grid.plus(newPos to newDist) else grid

                walk(remaining, newGrid, stack, newDist, newPos)
            }
        }
    }

    fun mapFacility(input: String) : ImmutableMap<Pos, Int> {
        val startGrid = immutableHashMapOf(Pos(0,0) to 0)
        val cleanedInput = input.drop(1).dropLast(1).toImmutableList()
        return walk(cleanedInput, startGrid, immutableListOf(), 0, Pos(0,0))
    }

    fun solve_pt1(input: String) : Int = mapFacility(input).values.sorted().last()

    fun solve_pt2(input: String) : Int = mapFacility(input).values.filter { it >= 1000 }.size

}