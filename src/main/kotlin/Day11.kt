object Day11 {

    data class Result(val x: Int, val y: Int, val squareSize: Int)

    fun powerLevel(x: Int, y: Int, serial: Int) : Int {
        val rackId = x + 10
        val c = ((rackId * y + serial) * rackId).toDouble()
        return (Math.floor(c / 100) % 10).toInt() - 5
    }

    fun calcGrid(serial: Int) : List<List<Int>> = (1 .. 300).map { y -> (1 .. 300).map { x: Int -> powerLevel(x,y,serial) } }

    fun sumSquare(xs: List<Int>, ys: List<Int>, grid: List<List<Int>>) = ys.fold(0) { accY, y -> xs.fold(accY) { accX, x -> accX + grid[y-1][x-1] } }

    fun bestSquareForSquareSize(squareSize: Int, grid: List<List<Int>>) : Pair<Result, Int> =
        (1 .. 300).windowed(squareSize,1).fold(Result(-1,-1,squareSize) to Int.MIN_VALUE) { accY: Pair<Result, Int>, yWindow: List<Int> ->
            (1 .. 300).windowed(squareSize,1).fold(accY) { accX, xWindow: List<Int> ->
                val total = sumSquare(xWindow, yWindow, grid)
                if (total > accX.second) Result(xWindow.first(), yWindow.first(), squareSize) to total
                else accX
            }
        }

    tailrec fun findBestSquare(squareSize: Int, bestPower: Int, bestCoord: Result, grid: List<List<Int>>) : Result {
        val highestForSize = bestSquareForSquareSize(squareSize, grid)
        if (highestForSize.second < 0 || squareSize == 300) return bestCoord
        val newBest = if (highestForSize.second > bestPower) highestForSize.first else bestCoord
        val newHighest = if (highestForSize.second > bestPower) highestForSize.second else bestPower
        return findBestSquare(squareSize+1, newHighest, newBest, grid)
    }

    fun solve_pt1(input: Int) = bestSquareForSquareSize(3, calcGrid(input)).first

    fun solve_pt2(input: Int) = findBestSquare(1, Int.MIN_VALUE, Result(-1,-1,-1), calcGrid(input))

    fun printGridMap(grid: List<List<Int>>) =
        (0 until 300).forEach { y -> (0 until 300).forEach { x -> print(if (grid[x][y] > 0) '#' else if (grid[x][y] < 0) '-' else ' ') }; println() }
}