import kotlinx.collections.immutable.*

object Day9 {

    tailrec fun addMarble(players: ImmutableMap<Int, Long>, marbles: ImmutableList<Int>, currentMarble: Int, currentIndex: Int, currentPlayer: Int, finalMarble: Int) : Long {
        val tryIndex = (currentIndex+1) % marbles.size
        val trySevenBehindIndex = tryIndex - 8
        val sevenBehindIndex = if (trySevenBehindIndex < 0) marbles.size + trySevenBehindIndex else trySevenBehindIndex
        val newPlayers = if (currentMarble % 23 == 0) {
            val currentScore = players[currentPlayer] ?: 0
            val sevenBehindScore = marbles[sevenBehindIndex]
            players.put(currentPlayer, currentScore + currentMarble + sevenBehindScore)
        } else players
        val newMarbles = if (currentMarble % 23 == 0) marbles.removeAt(sevenBehindIndex) else marbles.add(tryIndex+1, currentMarble)
        val nextIndex = if (currentMarble % 23 == 0) sevenBehindIndex-1 else tryIndex
        val nextPlayer = (currentPlayer+1) % players.size
        return if (currentMarble == finalMarble) newPlayers.values.sorted().last()
        else addMarble(newPlayers, newMarbles.toImmutableList(), currentMarble + 1, nextIndex+1, nextPlayer, finalMarble)
    }

    fun solve(players: Int, lastMarble: Int) : Long =
        addMarble((0 until players).associate { it to 0L }.toImmutableMap(), immutableListOf(0), 1, 0, 1, lastMarble)
}