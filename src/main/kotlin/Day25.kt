import kotlinx.collections.immutable.*

object Day25 {

    data class Star(val x: Int, val y: Int, val z: Int, val r: Int) {
        fun distanceFrom(other: Star) = Math.abs(x - other.x) + Math.abs(y - other.y) + Math.abs(z - other.z) + Math.abs(r - other.r)
    }

    fun parseInput(input: List<String>) : ImmutableList<Star> =
        input.fold(immutableListOf()) { acc, line ->
            val (x,y,z,r) = """(\S+),(\S+),(\S+),(\S+)""".toRegex().find(line)!!.destructured
            acc.plus(Star(x.toInt(),y.toInt(),z.toInt(),r.toInt()))
        }

    fun findConstellation(current: Star, toCheck: ImmutableList<Star>, constellation: ImmutableList<Star>, checked: ImmutableList<Star>) : ImmutableList<Star> {
        if (toCheck.isEmpty()) return constellation.plus(current)
        val inRange = toCheck.filter { !constellation.contains(it) && !checked.contains(it) && it.distanceFrom(current) <= 3}
        val branches = inRange.fold(constellation to checked) { (f, c), next ->
            val found = findConstellation(next, toCheck.minus(checked), f.plus(next), c.plus(next))
            f.plus(found).distinct().toImmutableList() to c.plus(found).distinct().toImmutableList()
        }
        return constellation.plus(current).plus(branches.first).distinct().toImmutableList()
    }

    tailrec fun findAllConstellations(stars: ImmutableList<Star>, found: ImmutableList<ImmutableList<Star>>) : ImmutableList<ImmutableList<Star>> {
        if (stars.isEmpty()) return found
        val current = stars.first()
        val result = findConstellation(current, stars.minus(current), immutableListOf(), immutableListOf())
        return findAllConstellations(stars.minus(result), found.add(result))
    }

    fun solve_pt1(input: List<String>) : Int = findAllConstellations(parseInput(input), immutableListOf()).size

}