import kotlinx.collections.immutable.*

object Day22 {

    data class Pos(val col: Int, val row: Int) {
        fun distanceTo(other: Pos) = Math.abs(col - other.col) + Math.abs(row - other.row)
        fun plus(other: Pos) = Pos(col + other.col, row + other.row)
    }

    val Mouth = Pos(0,0)

    data class Area(val pos: Pos, val erosionLevel: Int, val geoIndex: Int) {
        val type: Int by lazy { erosionLevel % 3 }
    }

    fun erosionLevel(a: Pos, depth: Int, state: ImmutableMap<Pos, Area>, target: Pos) = (geoIndex(a, target, state) + depth) % 20183

    fun geoIndex(a: Pos, target: Pos, state: ImmutableMap<Pos, Area>) : Int {
        return if (a == Mouth || a == target) 0
        else if (a.row == 0) a.col * 16807
        else if (a.col == 0) a.row * 48271
        else {
            val r = state[a.plus(LEFT)]!!
            val b = state[a.plus(UP)]!!
            r.erosionLevel * b.erosionLevel
        }
    }

    fun createArea(pos: Pos, depth: Int, target: Pos, grid: ImmutableMap<Pos, Area>) : Area {
        return Area(pos, erosionLevel(pos, depth, grid, target), geoIndex(pos, target, grid))
    }

    fun calcGrid(depth: Int, target: Pos, padding: Int): ImmutableMap<Pos, Area> {
        val startGrid = immutableHashMapOf<Pos, Area>()
        return (0 .. target.row+padding).fold(startGrid) { rowGrid, rowIndex ->
            (0 .. target.col+padding).fold(rowGrid) {colGrid, colIndex ->
                val newPos = Pos(colIndex, rowIndex)
                val newArea = createArea(newPos, depth, target, colGrid)
                colGrid.put(newPos, newArea)
            }
        }
    }

    fun solve_pt1(depth: Int, target: Pos) : Int {
        val endGrid = calcGrid(depth, target, 0)
        return endGrid.values.sumBy { it.type  }
    }

    data class State(val time: Int, val minDistance: Int, val pos: Pos, val tool: Int)

    const val NEITHER = 0
    const val TORCH = 1
    const val CLIMBING = 2

    val DOWN = Pos(0, 1)
    val UP = Pos(0, -1)
    val LEFT = Pos(-1, 0)
    val RIGHT = Pos(1, 0)

    tailrec fun navigate(grid: ImmutableMap<Pos, Area>, queue: ImmutableList<State>, seen: ImmutableMap<Pair<Pos, Int>, Int>, target: Pos, depth: Int) : Int {
        val state = queue.first()
        val qLeft = queue.drop(1).toImmutableList()
        val areaTypeOrNull = grid[state.pos]?.type
        val seenOrNull = seen[state.pos to state.tool]
        return if (state.pos == target) state.time + if (state.tool == TORCH) 0 else 7
        else if (areaTypeOrNull == null || areaTypeOrNull == state.tool) navigate(grid, qLeft, seen, target, depth)
        else if (seenOrNull != null && seenOrNull <= state.time) navigate(grid, qLeft, seen, target, depth)
        else {
            val newSeen = seen.plus((state.pos to state.tool) to state.time)

            val possibleMoves = listOf(DOWN, RIGHT, LEFT, UP).map {
                val newPos = state.pos.plus(it)
                if (newPos.col >= 0 && newPos.row >= 0)
                    State(state.time + 1, newPos.distanceTo(target), newPos, state.tool)
                else null
            }.filterNotNull()

            val possibleTools = listOf(NEITHER, TORCH, CLIMBING).minus(state.tool).map { State(state.time + 7, state.minDistance, state.pos, it) }
            val newQ = qLeft.plus(possibleMoves).plus(possibleTools).sortedBy { it.time }.toImmutableList()
            return navigate(grid, newQ, newSeen, target, depth)
        }
    }

    fun solve_pt2(depth: Int, target: Pos) : Int {
        val grid = calcGrid(depth, target, 50)
        val startQ = immutableListOf(State(0, Mouth.distanceTo(target), Mouth, TORCH))
        return navigate(grid, startQ, immutableHashMapOf(), target, depth)
    }
}