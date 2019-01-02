object Day23 {

    data class Pos(val x: Int, val y: Int, val z: Int) {
        fun distanceFrom(other: Pos) = Math.abs(x - other.x) + Math.abs(y - other.y) + Math.abs(z - other.z)
    }

    data class NanoBot(val pos: Pos, val r: Int)

    fun parseInput(input: List<String>) : List<NanoBot> =
        input.fold(emptyList<NanoBot>()) { acc, line ->
            val (x,y,z,r) = """<(\S+),(\S+),(\S+)>, r=(\S+)""".toRegex().find(line)!!.destructured
            acc + NanoBot(Pos(x.toInt(),y.toInt(),z.toInt()),r.toInt())
        }

    fun solve_pt1(input: List<String>) : Int {
        val bots = parseInput(input)
        val strongest = bots.sortedBy { it.r }.last()
        val inDistance = bots.filter { strongest.pos.distanceFrom(it.pos) <= strongest.r }
        return inDistance.size
    }

    tailrec fun walkTowardsOrigin(now: NanoBot, best: NanoBot, bots: List<NanoBot>) : NanoBot {
        val newPos = Pos(now.pos.x-1, now.pos.y-1, now.pos.z - 1)
        val newVal = NanoBot(newPos, bots.filter { it.pos.distanceFrom(newPos) <= it.r }.size)
        return if (now.r < best.r) best
        else walkTowardsOrigin(newVal, if (newVal.r > best.r) newVal else best, bots)
    }

    fun solve_pt2(input: List<String>) : Int {
        val bots = parseInput(input)
        val bestBot = bots.fold(NanoBot(Pos(0,0,0),0)) { best, now ->
            val inRange = bots.filter { it.pos.distanceFrom(now.pos) <= it.r }
            if (inRange.size > best.r) NanoBot(now.pos, inRange.size)
            else best
        }
        return walkTowardsOrigin(bestBot, bestBot, bots).pos.distanceFrom(Pos(0,0,0))
    }
}