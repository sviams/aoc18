import kotlinx.collections.immutable.*

typealias Path = ImmutableList<Day15.Coord>

object Day15 {

    val ReadingOrderComparator = compareBy<Coord>({it.y}, {it.x})

    fun List<Coord>.inReadingOrder() : ImmutableList<Coord> = this.sortedWith(ReadingOrderComparator).toImmutableList()

    fun GridPosition.toCoord() = Coord(this.first, this.second)

    val PathsComparator = compareBy<Path>({it.size}, {it.last()}, {it.first()})

    fun List<Path>.inCorrectOrder() : List<Path> = this.sortedWith(PathsComparator)

    data class Coord(val x: Int, val y: Int) : Comparable<Coord> {
        override fun compareTo(other: Coord): Int {
            return ReadingOrderComparator.compare(this, other)
        }

        fun freeSpaces(state: State) : List<Coord> =
            adjacentSpaces(state).filter { !state.goblins.any { g -> g.pos == it }  && !state.elves.any { e -> e.pos == it } }


        fun adjacentSpaces(state: State) : List<Coord> =
            listOf(Coord(x+1, y), Coord(x, y+1), Coord(x-1, y), Coord(x, y-1)).filter {state.cave[it.y][it.x] == 0}

        fun toGridPos() = GridPosition(x,y)

    }

    fun shortestPath(start: Coord, end: Coord, state: State) : Pair<Path, Int>? {
        val barriers = state.cave.mapIndexed { rowIndex, row -> row.mapIndexed { col, value -> if (value == 1) GridPosition(col, rowIndex) else null } }.flatten().filterNotNull().toSet()
        val startPos = GridPosition(start.x, start.y)
        val endPos = GridPosition(end.x, end.y)
        val elves = state.elves.map { it.pos.toGridPos() }.minus(startPos).minus(endPos).toSet()
        val gobs = state.goblins.map { it.pos.toGridPos() }.minus(startPos).minus(endPos).toSet()
        val allBarriers = listOf(barriers + elves + gobs)
        return try {
            val (p, cost) = aStarSearch(startPos, endPos, SquareGrid(state.cave[0].size, state.cave.size, allBarriers))
            p.map { it.toCoord() }.toImmutableList() to cost
        } catch (e: Exception) {
            null
        }
    }

    val CombatUnitComparator = compareBy<CombatUnit>({it.hp}, {it.pos})

    abstract class CombatUnit(val pos: Coord, val hp: Int, val num: Int, val ap: Int) : Comparable<CombatUnit> {

        abstract fun move(newPos: Coord, state: State) : State

        abstract fun attack(target: CombatUnit, state: State) : State

        abstract fun id() : String

        override fun compareTo(other: CombatUnit): Int = CombatUnitComparator.compare(this, other)

        override fun equals(other: Any?): Boolean = (other as CombatUnit).id() == id()
    }

    class Goblin(pos: Coord, hp: Int, num: Int) : CombatUnit(pos, hp, num, 3) {

        override fun id(): String = "G$num"

        override fun toString(): String = "${id()} $pos $hp"

        override fun move(newPos: Coord, state: State) : State {
            val newGobs = state.goblins.minus(this).plus(Goblin(newPos, hp, num))
            return State(newGobs, state.elves, state.cave)
        }

        override fun attack(target: CombatUnit, state: State) : State {
            val newElves = state.elves.minus(target)
            val afterElimination = if (target.hp > this.ap) newElves.plus(Elf(target.pos, target.hp - this.ap, target.num, target.ap)) else newElves
            return State(state.goblins, afterElimination as List<Elf>, state.cave)
        }
    }

    class Elf(pos: Coord, hp: Int, num: Int, ap: Int) : CombatUnit(pos, hp, num, ap) {

        override fun id(): String = "E$num"

        override fun toString(): String = "${id()} $pos $hp"

        override fun move(newPos: Coord, state: State) : State {
            val newElves = state.elves.minus(this).plus(Elf(newPos, hp, num, ap))
            return State(state.goblins, newElves, state.cave)
        }

        override fun attack(target: CombatUnit, state: State) : State {
            val newGobs = state.goblins.minus(target)
            val afterElimination = if (target.hp > this.ap) newGobs.plus(Goblin(target.pos, target.hp - this.ap, target.num)) else newGobs
            return State(afterElimination as List<Goblin>, state.elves, state.cave)
        }

    }

    data class State(val goblins: List<Goblin>, val elves: List<Elf>, val cave: ImmutableList<ImmutableList<Int>>) {

        fun print() {
            println()
            val height = cave.size
            val width = cave[0].size
            (0 until height).forEach { row ->
                (0 until width).forEach { col ->
                    val p = Coord(col, row)
                    when {
                        goblins.any { it.pos == p } -> print('G')
                        elves.any { it.pos == p} -> print('E')
                        cave[row][col] == 1 -> print('#')
                        else -> print('.')
                    }
                }
                println()
            }
            println()
        }
    }

    fun parseInput(input: List<String>, elfPower: Int) : State {
        var gobCount = 0
        var elfCount = 0
        return input.foldIndexed(State(emptyList(), emptyList(), immutableListOf())) { row, rowAcc, line ->
            val caveParts = line.map { ch -> if (ch == '#') 1 else 0 }.toImmutableList()
            val gobs = line.mapIndexed { col, ch -> if (ch == 'G') Goblin(Coord(col, row), 200, gobCount++) else null }.filterNotNull()
            val elves = line.mapIndexed { col, ch -> if (ch == 'E') Elf(Coord(col, row), 200, elfCount++, elfPower) else null }.filterNotNull()
            State(rowAcc.goblins.plus(gobs), rowAcc.elves.plus(elves), rowAcc.cave.add(caveParts))
        }
    }

    fun moveUnit(u: CombatUnit, enemies: List<CombatUnit>, state: State) : State {
        val myAdjacents = u.pos.adjacentSpaces(state)
        val enemiesInRange = enemies.filter { myAdjacents.contains(it.pos) }.sorted()
        if (enemiesInRange.any()) return u.attack(enemiesInRange.first(), state)

        val freeEnemySpaces = enemies.flatMap { it.pos.freeSpaces(state) }.inReadingOrder()
        val freeOwnSpaces = u.pos.freeSpaces(state)
        val pathsToEnemySpaces = freeEnemySpaces.flatMap { es -> freeOwnSpaces.map { os -> es to shortestPath(os, es, state) }   }.filter { it.second != null }
        if (pathsToEnemySpaces.isEmpty()) return state

        val allPathsWithDistance = pathsToEnemySpaces.map { it.second }.filterNotNull()
        val allPaths = allPathsWithDistance.map { it.first }.inCorrectOrder()
        val newPos = allPaths.first().first() // First step on best path

        val afterMove = u.move(newPos, state)
        val newAdjacents = newPos.adjacentSpaces(afterMove)
        val newInRange = enemies.filter { newAdjacents.contains(it.pos) }.sortedBy { it }
        return if (newInRange.any()) u.attack(newInRange.first(), afterMove)
        else afterMove
    }

    tailrec fun moveUnits(hasMoved: List<String>, state: State) : Pair<State, Boolean> {
        val toMove = state.elves.plus(state.goblins).filter { !hasMoved.contains(it.id()) }.sortedBy { it.pos }
        if (toMove.isEmpty()) return state to true
        val next = toMove.first()
        val enemies = if (next is Elf) state.goblins else state.elves
        if (enemies.isEmpty()) return state to false
        val afterMove = moveUnit(next, enemies, state)
        return moveUnits(hasMoved.plus(next.id()), afterMove)
    }

    tailrec fun evolve(state: State, count: Int, acceptElfLosses : Boolean) : Pair<State, Int> {
        val (nextState, fullRound) = moveUnits(emptyList(), state)
        return if (nextState.elves.size != state.elves.size && !acceptElfLosses) nextState to -1
        else if (nextState.goblins.isEmpty() || nextState.elves.isEmpty()) nextState to if (fullRound)count+1 else count
        else evolve(nextState, count +1, acceptElfLosses)
    }

    fun solve_pt1(input: List<String>) : Int {
        val startState = parseInput(input, 3)
        val (endState, rounds) = evolve(startState, 0, true)
        return rounds * (endState.goblins.sumBy { it.hp } + endState.elves.sumBy { it.hp })
    }

    tailrec fun findThePower(input: List<String>, thePower: Int) : Int {
        println("Testing $thePower")
        val startState = parseInput(input, thePower)
        val (endState, rounds) = evolve(startState, 0, false)
        return if (rounds > 0) rounds * (endState.elves.sumBy { it.hp } + endState.goblins.sumBy { it.hp })
        else findThePower(input, thePower + 1)
    }

    fun solve_pt2(input: List<String>) : Int = findThePower(input, 10)
}