object Day2 {

    fun solve_pt1(input: List<String>) : Int {
        val total = input.fold(Pair(0,0)) {acc, word ->
             val res = hitsInWord(word.toCharArray())
             Pair(acc.first + res.first, acc.second + res.second)
         }
        return total.first * total.second
    } // ~1 ms

    fun hitsInWord(word: CharArray) : Pair<Int, Int> {
        val freqMap = word.associate { it to word.sumBy { c -> if (c == it) 1 else 0 }}
        val pair = if (freqMap.containsValue(2)) 1 else 0
        val triplet = if (freqMap.containsValue(3)) 1 else 0
        return Pair(pair, triplet)
    }

    fun sameInWords(a: CharArray, b: CharArray) : String = a.foldIndexed("") {index, acc, char -> if (b[index] == char) acc + char else acc }

    fun solve_pt2(input: List<String>) : String {
        input.forEach { a -> input.forEach { b ->
            val same = sameInWords(a.toCharArray(),b.toCharArray())
            if (same.length == a.length - 1) return same
        } }
        return "FOO"
    } // ~7 ms

}