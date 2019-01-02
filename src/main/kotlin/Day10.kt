import kotlinx.collections.immutable.*

object Day10 {

    data class Star(val x: Int, val y: Int, val dx: Int, val dy: Int)

    data class Heaven(val stars: List<Star>, val age: Int)

    fun parseInput(input: List<String>) : List<Star> {
        return input.fold(emptyList<Star>()) { acc, line ->
            val parts = line.split("> ")
            val (x,y) = """position=<\s*(\S+),\s*(\S+)""".toRegex().find(parts[0])!!.destructured
            val (dx,dy) = """velocity=<\s*(\S+),\s*(\S+)>""".toRegex().find(parts[1])!!.destructured
            acc + Star(x.toInt(),y.toInt(),dx.toInt(),dy.toInt())
        }

    }

    fun minMax(stars: List<Star>) : Pair<IntRange, IntRange> =
        (stars.minBy { it.x }!!.x .. stars.maxBy { it.x }!!.x) to (stars.minBy { it.y }!!.y .. stars.maxBy { it.y }!!.y)

    fun area(stars: List<Star>) : Long {
        val range = minMax(stars)
        return Math.abs(range.first.last - range.first.first).toLong() * Math.abs(range.second.last - range.second.first).toLong()
    }

    fun printStars(stars: List<Star>) {
        val range = minMax(stars)
        range.second.forEach { y ->
            range.first.forEach { x ->
                print(if (stars.any { it.x == x && it.y == y }) '#' else '.')
            }
            println()
        }
    }

    tailrec fun findMessage(stars: List<Star>, area: Long, age: Int) : Heaven {
        val newStars = stars.map { Star(it.x + it.dx, it.y + it.dy, it.dx, it.dy) }
        val newArea = area(newStars)
        return if (newArea > area) Heaven(stars, age)
        else findMessage(newStars, newArea, age + 1)
    }

    fun solve_pt1(input: List<String>) : Int {
        val stars = parseInput(input)
        printStars(findMessage(stars, area(stars), 0).stars)
        return -1
    }

    fun solve_pt2(input: List<String>) : Int {
        val stars = parseInput(input)
        return findMessage(stars, area(stars), 0).age
    }
}