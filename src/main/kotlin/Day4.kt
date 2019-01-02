import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap

object Day4 {

    data class Record(val date: String, val time: String, val activity: String) {
        val isChange = activity.startsWith("Guard")
        fun minute() = time.substring(3,5).toInt()
        fun guardId() : Int {
            val (idString) = """Guard #(\d+) begins shift""".toRegex().find(activity)!!.destructured
            return idString.toInt()
        }
    }

    data class GuardRecord(val timesAsleep: List<IntRange>, val id: Int)

    data class ParserState(val guardRecords: ImmutableList<GuardRecord>, val input: List<Record>)

    data class GuardStats(val id: Int, val totalSleepTime: Int, val mostFrequentSleepMinute: Int, val maxSleepFreq: Int) {
        val score = id * mostFrequentSleepMinute
    }

    fun parseRecord(row: String) : Record {
        val (d,t, a) = """\[(\S+) (\S+)] (.+)""".toRegex().find(row)!!.destructured
        return Record(d,t,a)
    }

    fun takeOneGuardRecord(input: List<Record>) : Pair<GuardRecord, List<Record>> {
        if (input.isEmpty()) return Pair(GuardRecord(emptyList(), -1), emptyList())
        val changeRecord = input.first()
        val chunk = input.minus(changeRecord).takeWhile { !it.isChange }.toList()
        val sleepTimes = chunk.windowed(2, 2).map { it.first().minute() until it.last().minute() }.toList()
        return Pair(GuardRecord(sleepTimes, changeRecord.guardId()), input.minus(changeRecord).minus(chunk))
    }

    fun parseGuards(input: List<Record>) : ImmutableList<GuardRecord> =
        generateSequence(ParserState(emptyList<GuardRecord>().toImmutableList(), input)) { state ->
            val (newGuardRecord, restOfInput) = takeOneGuardRecord(state.input)
            ParserState(state.guardRecords.add(newGuardRecord), restOfInput)
        }.takeWhileInclusive { it.input.isNotEmpty() }.last().guardRecords

    fun frequencyMap(records: Map<Int, List<GuardRecord>>) : List<GuardStats> =
        records.map { (id, recs) ->
            val allSleeps = recs.fold(emptyList<IntRange>()) { acc, record -> acc + record.timesAsleep}.flatMap { it.asIterable() }
            if (allSleeps.isEmpty()) GuardStats(0,0,0, 0)
            else {
                val freqMap: ImmutableMap<Int, Int> = allSleeps.fold(emptyMap<Int, Int>().toImmutableMap()) { acc, min ->
                    acc.put(min, (acc[min] ?: 0) + 1)
                }
                val (mostFrequentMin, frequency) = freqMap.toList().sortedBy { it.second }.last()
                GuardStats(id, allSleeps.size, mostFrequentMin, frequency)
            }

        }

    fun allRecordsByGuard(input: List<String>) = parseGuards(input.sorted().map { parseRecord(it) }).groupBy { it.id }

    fun solve_pt1(input: List<String>) : Int =
        frequencyMap(allRecordsByGuard(input)).sortedBy { it.totalSleepTime }.last().score

    fun solve_pt2(input: List<String>) : Int =
        frequencyMap(allRecordsByGuard(input)).sortedBy { it.maxSleepFreq }.last().score

}