import kotlinx.collections.immutable.*

object Day7 {

    fun parseInput(input: List<String>) : List<BacklogItem> =
        input.fold(immutableHashMapOf<Char, String>()) { acc, line ->
            val (a,b) = """Step (\S+) must be finished before step (\S+) can begin.""".toRegex().find(line)!!.destructured
            acc.put(b.single(), (acc[b.single()] ?: "") + a).put(a.single(), acc[a.single()] ?: "")
        }.map { (k,v) -> BacklogItem(k,v) }

    data class BacklogItem(val id: Char, val dependencies: String)
    data class InProgressItem(val id: Char, val remaining: Int)

    tailrec fun doWork(backlog: List<BacklogItem>, result: String) : String {
        val nextId = backlog.sortedBy { it.dependencies.length }.first().id
        val afterWork = backlog.filter { it.id != nextId }.map { (id, deps) -> BacklogItem(id, deps.filter { it != nextId }) }
        return if (afterWork.isEmpty()) result + nextId else doWork(afterWork, result + nextId)
    }

    fun solve_pt1(input: List<String>) : String = doWork(parseInput(input), "")

    tailrec fun doWorkDeluxe(backlog: List<BacklogItem>, inProgress: List<InProgressItem>, time: Int, elves: Int, offset: Int) : Int {
        val candidates = backlog.filter { it.dependencies.isEmpty() }
        val toCapacity = if (candidates.size + inProgress.size > elves) candidates.take(elves - inProgress.size) else candidates
        val filledInProgress = inProgress + toCapacity.map { (id, _) -> InProgressItem(id, id.toInt() -64 + offset) }
        val afterWork = filledInProgress.map { (id, remaining) -> InProgressItem(id, remaining - 1) }
        val stillInProgress = afterWork.filter { it.remaining > 0 }
        val finishedIds = (afterWork - stillInProgress).map { it.id }
        val updatedBacklog = (backlog - toCapacity).map { (id, deps) -> BacklogItem(id, deps.filter { !finishedIds.contains(it) }) }
        return if (backlog.isEmpty() && stillInProgress.isEmpty()) time + 1
        else doWorkDeluxe(updatedBacklog, stillInProgress, time + 1, elves, offset)
    }

    fun solve_pt2(input: List<String>, elves: Int, offset: Int) : Int = doWorkDeluxe(parseInput(input), emptyList(), 0, elves, offset)
}