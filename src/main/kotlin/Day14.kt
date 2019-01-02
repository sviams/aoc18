import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.immutableListOf
import kotlinx.collections.immutable.plus
import kotlinx.collections.immutable.toImmutableList

object Day14 {

    tailrec fun findRecipes(current: ImmutableList<Int>, ai: Int, bi: Int, count: Int, cutoff: Int) : String {
        val aVal = current[ai]
        val bVal = current[bi]
        val sum = aVal + bVal
        val one = sum % 10
        val newCurrent = if (sum > 9) current.plus((Math.floor(sum.toDouble() / 10) % 10).toInt()).plus(one) else current.plus(one)
        val newAi = (ai + 1 + aVal) % newCurrent.size
        val newBi = (bi + 1 + bVal) % newCurrent.size
        return if (newCurrent.size >= (cutoff + 10)) if (sum > 9) newCurrent.dropLast(1).takeLast(10).joinToString("") else newCurrent.takeLast(10).joinToString("")
        else findRecipes(newCurrent, newAi, newBi, count + 1, cutoff)
    }

    tailrec fun findRecipes2(current: ImmutableList<Int>, ai: Int, bi: Int, count: Int, pattern: ImmutableList<Int>) : Int {
        val aVal = current[ai]
        val bVal = current[bi]
        val sum = aVal + bVal
        val one = sum % 10
        val newCurrent = if (sum > 9) current.plus((Math.floor(sum.toDouble() / 10) % 10).toInt()).plus(one) else current.plus(one)
        val newAi = (ai + 1 + aVal) % newCurrent.size
        val newBi = (bi + 1 + bVal) % newCurrent.size
        if (count % 1000000 == 0) println(count)
        return if (newCurrent.takeLast(pattern.size+1).joinToString("").contains(pattern.joinToString(""))) {
            println(newCurrent.takeLast(10))
            newCurrent.size - pattern.size
        }
        else findRecipes2(newCurrent, newAi, newBi, count + 1, pattern)
    }

    fun solve_pt1(input: Int) : String {
        val start = immutableListOf(3,7)
        return findRecipes(start, 0, 1, 0, input)
    }

    fun solve_pt2(input: String) : Int {
        val start = immutableListOf(3,7)
        return findRecipes2(start, 0, 1, 0, input.toCharArray().map { it.toInt() - 48 }.toImmutableList())
    }

}