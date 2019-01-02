import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

object Day5 {

    data class State(val acc: ImmutableList<Char>, val skipNext: Boolean)

    fun pairFound(a: Char, b: Char): Boolean = when {
        a == b -> false
        a.toLowerCase() == b -> true // Aa
        else -> a.toUpperCase() == b // aA
    }

    fun removePolymer(word: String, index: Int) : String = word.substring(0, index) + word.substring(index + 2, word.length)

    fun findFirstPolymerIndex(word: String) : Int = word.windowed(2).indexOfFirst { pairFound(it[0], it[1]) }

    fun isPolymerAtIndex(word: String, index: Int) : Boolean {
        if (index == -1) return false
        val chars = word.substring(index, index+2)
        return pairFound(chars[0], chars[1])
    }

    // Fairly quickly reduces the input from 50k to 12k chars, not so efficient after that
    tailrec fun shotgun(word: String): String {
        val newWord = word.foldIndexed(State("".toImmutableList(), false)) { index, state, c ->
            if (state.skipNext) State(state.acc, false)
            else if (index >= word.length - 1 || !pairFound(c, word[index + 1])) State(state.acc.add(c), false)
            else State(state.acc, true)
        }.acc.joinToString("")
        return if (newWord.length == word.length - 2) word
        else shotgun(newWord)
    }

    // Excises polymers at the same index
    tailrec fun scalpel(word: String, index: Int) : String {
        return if (!isPolymerAtIndex(word, index)) word
        else scalpel(removePolymer(word, index), if (index > 0) index-1 else 0)
    }

    fun solve_pt1(input: String) : Int {
        val rough = shotgun(input)
        val index = findFirstPolymerIndex(rough)
        if (index == -1) return rough.length // Only needed for sample input
        return scalpel(rough, index).length
    }

    fun solve_pt2(input: String) : Int =
        ('a' .. 'z').map { char -> solve_pt1(input.filter { it.toLowerCase() != char }) }.min()!!
}