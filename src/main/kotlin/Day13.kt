import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableHashMap

object Day13 {

    data class Cart(val pos: Coord, val nextTurn : Int, val dir: Int)

    data class Coord(val col: Int, val row: Int) : Comparable<Coord> {
        override fun compareTo(other: Coord): Int {
            return if (other.row == row) col - other.col else row - other.row
        }

        fun right() = Coord(col + 1, row)
        fun down() = Coord(col, row + 1)
        fun left() = Coord(col - 1, row)
        fun up() = Coord(col, row - 1)
    }

    fun parseCarts(input: List<String>) : List<Cart> =
        input.foldIndexed(emptyList()) { rowIndex, accRow, line ->
            accRow + line.foldIndexed(emptyList<Cart>()) { colIndex, colAcc, c ->
                val pos = Coord(colIndex, rowIndex)
                when (c) {
                    '>' -> colAcc + Cart(pos, 0, 0)
                    'v' -> colAcc + Cart(pos, 0, 1)
                    '<' -> colAcc + Cart(pos, 0, 2)
                    '^' -> colAcc + Cart(pos, 0, 3)
                    else -> colAcc
                }
            }
        }

    private fun parseTracks(input: List<String>): Map<Coord, Int> =
        input.foldIndexed(emptyMap()) {rowIndex, rowAcc, line ->
            rowAcc + line.foldIndexed(emptyMap<Coord, Int>()) { colIndex, colAcc, c ->
                val pos = Coord(colIndex, rowIndex)
                when (c) {
                    '-' -> colAcc + (pos to 0)
                    '|' -> colAcc + (pos to 1)
                    '/' -> colAcc + (pos to 2)
                    '\\' -> colAcc + (pos to 3)
                    '+' -> colAcc + (pos to 4)
                    '>' -> colAcc + (pos to 0)
                    '<' -> colAcc + (pos to 0)
                    '^' -> colAcc + (pos to 1)
                    'v' -> colAcc + (pos to 1)
                    else -> colAcc
                }
            }
        }

    fun moveCart(c: Cart, tracks: Map<Coord, Int>) : Cart {
        val newPos = when (c.dir) {
            0 -> c.pos.right()
            1 -> c.pos.down()
            2 -> c.pos.left()
            3 -> c.pos.up()
            else -> c.pos
        }
        val newDir = when (tracks[newPos]) {
            2 -> Math.abs(c.dir -3) // '/'
            3 -> when (c.dir) { // '\'
                0 -> 1
                1 -> 0
                2 -> 3
                3 -> 2
                else -> c.dir
            }
            4 -> when (c.nextTurn) { // '+'
                0 -> if (c.dir-1 < 0) 3 else c.dir - 1
                1 -> c.dir
                2 -> (c.dir +1) % 4
                else -> c.dir
            }
            else -> c.dir
        }
        val nextTurn = if (tracks[newPos] == 4) (c.nextTurn + 1) % 3 else c.nextTurn
        return Cart(newPos, nextTurn, newDir)
    }

    tailrec fun moveCarts(toMove: List<Cart>, hasMoved: List<Cart>, collisions: List<Cart>, tracks: Map<Coord, Int>) : Pair<List<Cart>, List<Cart>> {
        val next = toMove.sortedBy { it.pos }.first()
        val moved = moveCart(next, tracks)
        val possibleCollision = collidesWith(moved, toMove + hasMoved)
        val toMoveAfterCollision = (if (possibleCollision != null) toMove.minus(possibleCollision) else toMove).minus(next)
        val hasMovedAfterCollision = if (possibleCollision != null) hasMoved.minus(possibleCollision) else hasMoved.plus(moved)
        val newCollisions = if (possibleCollision != null) collisions.plus(possibleCollision).plus(moved) else collisions
        return if (toMoveAfterCollision.isEmpty()) hasMovedAfterCollision to newCollisions
        else moveCarts(toMoveAfterCollision, hasMovedAfterCollision, newCollisions, tracks)
    }

    fun collidesWith(c: Cart, all: List<Cart>) : Cart? {
        return all.minus(c).singleOrNull { it.pos == c.pos }
    }

    tailrec fun findFirstCrash(carts: List<Cart>, tracks: Map<Coord, Int>, tick: Int) : Coord {
        val newCarts = moveCarts(carts, emptyList(), emptyList(), tracks)
        return if (newCarts.second.any()) newCarts.second.firstOrNull()?.pos ?: Coord(-1, -1)
        else findFirstCrash(newCarts.first, tracks, tick + 1)
    }

    tailrec fun findLastCart(carts: List<Cart>, tracks: Map<Coord, Int>, tick: Int) : Coord {
        val newCarts = moveCarts(carts, emptyList(), emptyList(), tracks)
        return if (newCarts.first.size == 1) newCarts.first.single().pos
        else findLastCart(newCarts.first, tracks, tick + 1)
    }

    fun solve_pt1(input: List<String>) : String {
        val carts : List<Cart> = parseCarts(input)
        val tracks : Map<Coord, Int> = parseTracks(input)
        val res = findFirstCrash(carts, tracks, 0)
        return "${res.col},${res.row}"
    }

    fun solve_pt2(input: List<String>) : String {
        val carts : List<Cart> = parseCarts(input)
        val tracks : Map<Coord, Int> = parseTracks(input)
        val res = findLastCart(carts, tracks, 0)
        return "${res.col},${res.row}"
    }

}

object Day13Debug {

    fun trackTypeToChar(type: Int) : Char = when (type) {
        0 -> '-'
        1 -> '|'
        2 -> '/'
        3 -> '\\'
        4 -> '+'
        else -> ' '
    }

    fun dirToChar(dir: Int) : Char = when (dir) {
        0 -> '>'
        1 -> 'v'
        2 -> '<'
        3 -> '^'
        else -> 'Ä'
    }

    fun printState(carts: List<Day13.Cart>, tracks: Map<Day13.Coord, Int>) {
        println()
        val maxRow = tracks.keys.sortedBy { it.row }.last().row
        val maxCol = tracks.keys.sortedBy { it.col }.last().col
        (0 .. maxRow).forEach { row ->
            (0 .. maxCol).forEach { col ->
                val pos = Day13.Coord(col, row)
                val c = if (carts.any { c -> c.pos == pos }) {
                    dirToChar(carts.first { it.pos == pos }.dir)
                } else {
                    trackTypeToChar(tracks[pos] ?: -1)
                }
                print(c)
            }
            println()
        }
    }
}