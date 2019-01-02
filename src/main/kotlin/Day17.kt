object Day17 {

    data class Pos(val col: Int, val row: Int) {
        fun below() = Pos(col, row+1)
        fun above() = Pos(col, row-1)
    }

    data class Vein(val cols: IntRange, val rows: IntRange) {
        fun contains(pos: Pos) : Boolean = cols.contains(pos.col) && rows.contains(pos.row)
    }

    fun parseInput(input: List<String>) : List<Vein> =
        input.fold(emptyList()) { acc, line ->
            if (line.startsWith('x')) {
                val (x,y1,y2) = """x=(\S+), y=(\S+)\.\.(\S+)""".toRegex().find(line)!!.destructured
                acc + Vein(x.toInt() .. x.toInt(),y1.toInt() .. y2.toInt())
            } else {
                val (y,x1,x2) = """y=(\S+), x=(\S+)\.\.(\S+)""".toRegex().find(line)!!.destructured
                acc + Vein(x1.toInt() .. x2.toInt(),y.toInt() .. y.toInt())
            }

        }

    data class State(val veins: List<Vein>, val running: List<Pos>, val settled: List<Pos>) {

        val leftEdge : Int by lazy { veins.sortedBy { it.cols.start }.first().cols.start }
        val rightEdge : Int by lazy { veins.sortedByDescending { it.cols.endInclusive }.first().cols.endInclusive }

        fun print() {
            val cutoff = veins.sortedBy { it.rows.last }.last().rows.last
            (0 .. cutoff+1).forEach { row ->
                (leftEdge-5 .. rightEdge + 5).forEach { col ->
                    when {
                        settled.contains(Pos(col,row)) -> print('~')
                        running.contains(Pos(col,row)) -> print('|')
                        veins.any { it.cols.contains(col) and it.rows.contains(row) } -> print("#")
                        else -> print('.')
                    }
                }
                println()
            }
            println()
        }
    }

    tailrec fun fillHoriz(state: State, obstacles: List<Vein>, tip: Pos, dir: Int) : Pair<Pos, Boolean> {
        val newTip = Pos(tip.col + dir, tip.row)
        return when {
            obstacles.any { it.contains(newTip) } -> tip to true
            obstacles.none { it.contains(newTip.below()) } && !state.settled.contains(newTip.below()) -> newTip to false
            else -> fillHoriz(state, obstacles, newTip, dir)
        }
    }

    tailrec fun fill(state: State, tip: Pos) : Pair<State, List<Pos?>> {
        val newTip = tip.above()
        val obstacles = state.veins.filter { it.rows.contains(newTip.row) || it.rows.contains(tip.row) }
        val (leftTip, settledLeft) = fillHoriz(state, obstacles, newTip, -1)
        val (rightTip, settledRight) = fillHoriz(state, obstacles, newTip, 1)
        return if (!(settledLeft && settledRight)) {
            val newRunning = (leftTip.col .. rightTip.col).map { Pos(it, newTip.row) }
            State(state.veins, state.running.plus(newRunning), state.settled.minus(newRunning)) to listOf(if (!settledLeft) leftTip else null, if (!settledRight) rightTip else null)
        }
        else {
            val newSettled = (leftTip.col .. rightTip.col).map { Pos(it, newTip.row) }
            fill(State(state.veins, state.running.minus(newSettled), state.settled.plus(newSettled)), newTip)
        }
    }

    tailrec fun pour(state: State, tip: Pos, cutoff: Int, depth: Int) : State {
        val newTip = tip.below()
        if (newTip.row > cutoff) return state
        return if (state.running.contains(newTip)) state
        else if (state.veins.any { it.contains(newTip) } || state.settled.contains(newTip)) {
            val (stateAfterFill, possibleNewTips) = fill(state, newTip)
            val newTips = possibleNewTips.filterNotNull()
            newTips.fold(stateAfterFill) { s, t ->
                val ap = pour(s,t, cutoff, depth + 1)
                State(s.veins, s.running.plus(ap.running).distinct(), s.settled.plus(ap.settled).distinct())
            }
        } else pour(State(state.veins, state.running.plus(newTip), state.settled), newTip, cutoff, depth)
    }

    fun calculateEndState(input: List<String>) : State {
        val start = State(parseInput(input), emptyList(), emptyList())
        val maxRow = start.veins.sortedBy { it.rows.last }.last().rows.last
        val minRow = start.veins.sortedBy { it.rows.first }.first().rows.first
        val endState = pour(start, Pos(500,0), maxRow, 0)
        val finalSettled = endState.settled.filter { it.row >= minRow }.distinct()
        val finalRunning = endState.running.filter { it.row >= minRow }.minus(finalSettled).distinct()
        return State(start.veins, finalRunning, finalSettled)
    }

    fun solve_pt1(input: List<String>) : Int {
        val endState = calculateEndState(input)
        return endState.running.size + endState.settled.size
    }

    fun solve_pt2(input: List<String>) : Int {
        val endState = calculateEndState(input)
        return endState.settled.size
    }
}