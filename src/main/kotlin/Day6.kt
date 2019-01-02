import kotlinx.collections.immutable.*

object Day6 {

    data class Place(val col: Int, val row: Int, val area: Int) {
        fun distanceTo(row: Int, col: Int) = Math.abs(this.col - col) + Math.abs(this.row - row)
    }

    data class Parameters(val places: ImmutableSet<Place>) {
        val colRange = (places.minBy { it.col }!!.col .. places.maxBy { it.col }!!.col)
        val rowRange = (places.minBy { it.row }!!.row .. places.maxBy { it.row }!!.row)

        fun scoreTotal(limit: Int) : Int =
            rowRange.fold(0) { rowAcc, row ->
                colRange.fold(rowAcc) { colAcc, col ->
                    if (places.sumBy { it.distanceTo(row, col) } < limit) colAcc + 1 else colAcc
                }
            }

        fun scorePoints(rowRange: IntRange, colRange: IntRange) : ImmutableSet<Place> =
            rowRange.fold(places) { rowAcc, row ->
                colRange.fold(rowAcc) { colAcc, col ->
                    val closestOrNull = nearestPlaceOrNull(row, col, colAcc)
                    if (closestOrNull != null) colAcc.minus(closestOrNull).plus(Place(closestOrNull.col, closestOrNull.row, closestOrNull.area+1))
                    else colAcc
                }
            }
    }

    fun parseParameters(input: List<String>) =
        Parameters(input.map { line ->
            val (col, row) = """(\d+), (\d+)""".toRegex().find(line)!!.destructured
            Place(col.toInt(), row.toInt(), 0)
        }.toImmutableSet())

    fun nearestPlaceOrNull(row: Int, col: Int, points: ImmutableSet<Place>) : Place? {
        val out = points.associate { it to it.distanceTo(row, col) }
        val min = out.values.min()
        return out.filter { (k,v) -> v == min }.keys.singleOrNull()
    }



    fun solve_pt1(input: List<String>) : Int {
        val p = parseParameters(input)
        val allScores = p.scorePoints(p.rowRange, p.colRange).toList()
        val infsLeft = p.scorePoints(p.rowRange, (p.colRange.first .. p.colRange.first)).filter { it.area > 0 }
        val infsRight = p.scorePoints(p.rowRange, (p.colRange.last .. p.colRange.last)).filter { it.area > 0 }
        val infsTop = p.scorePoints((p.rowRange.first .. p.rowRange.first), p.colRange).filter { it.area > 0 }
        val infsBottom = p.scorePoints((p.rowRange.last .. p.rowRange.last), p.colRange).filter { it.area > 0 }
        val finites = allScores - infsLeft - infsRight - infsTop - infsBottom
        val sorted = finites.sortedByDescending { it.area }
        return sorted.drop(1).first().area // drop needed unless equals and hashCode are overriden to discount area in Place
    } // ~300ms

    fun solve_pt2(input: List<String>, limit: Int) : Int = parseParameters(input).scoreTotal(limit) // ~60ms
}