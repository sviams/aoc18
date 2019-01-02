import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.immutableHashMapOf
import kotlinx.collections.immutable.toImmutableHashMap

typealias Registers = ImmutableMap<Int, Int>

object Day16 {

    data class Sample(val before: Registers, val op: Int, val a: Int, val b: Int, val c: Int, val after: Registers)

    fun parseSamples(input: List<String>) : List<Sample> {
        return input.filterNot { it.isEmpty() }.chunked(3) { lines ->
            val bef = """Before:\s+\[(\S+), (\S+), (\S+), (\S+)]""".toRegex().find(lines[0])!!.destructured.toList()
                .mapIndexed { index, s -> index to s.toInt() }.toMap().toImmutableHashMap()
            val (op, a, b, c) = """(\S+) (\S+) (\S+) (\S+)""".toRegex().find(lines[1])!!.destructured
            val aft = """After:\s+\[(\S+), (\S+), (\S+), (\S+)]""".toRegex().find(lines[2])!!.destructured.toList()
                .mapIndexed { index, s -> index to s.toInt() }.toMap().toImmutableHashMap()
            Sample(bef, op.toInt(), a.toInt(), b.toInt(), c.toInt(), aft)
        }
    }

    data class Op(val code: Int, val a: Int, val b: Int, val c: Int)

    fun parseProgram(input: List<String>) : List<Op> =
        input.fold(emptyList()) { acc, line ->
            val (op, a, b, c) = """(\S+) (\S+) (\S+) (\S+)""".toRegex().find(line)!!.destructured
            acc + Op(op.toInt(), a.toInt(), b.toInt(), c.toInt())
        }

    val ops = immutableHashMapOf(
        0 to {s: Registers, a: Int, b: Int, c: Int -> s.put(c, s[a]!! + s[b]!!)},
        1 to {s: Registers, a: Int, b: Int, c: Int -> s.put(c, s[a]!! + b)},
        2 to {s: Registers, a: Int, b: Int, c: Int -> s.put(c, s[a]!! * s[b]!!)},
        3 to {s: Registers, a: Int, b: Int, c: Int -> s.put(c, s[a]!! * b)},
        4 to {s: Registers, a: Int, b: Int, c: Int -> s.put(c, s[a]!! and s[b]!!)},
        5 to {s: Registers, a: Int, b: Int, c: Int -> s.put(c, s[a]!! and b)},
        6 to {s: Registers, a: Int, b: Int, c: Int -> s.put(c, s[a]!! or s[b]!!)},
        7 to {s: Registers, a: Int, b: Int, c: Int -> s.put(c, s[a]!! or b)},
        8 to {s: Registers, a: Int, b: Int, c: Int -> s.put(c, s[a]!!)},
        9 to {s: Registers, a: Int, b: Int, c: Int -> s.put(c, a)},
        10 to {s: Registers, a: Int, b: Int, c: Int -> s.put(c, if (a > s[b]!!) 1 else 0)},
        11 to {s: Registers, a: Int, b: Int, c: Int -> s.put(c, if (s[a]!! > b) 1 else 0)},
        12 to {s: Registers, a: Int, b: Int, c: Int -> s.put(c, if (s[a]!! > s[b]!!) 1 else 0)},
        13 to {s: Registers, a: Int, b: Int, c: Int -> s.put(c, if (a == s[b]!!) 1 else 0)},
        14 to {s: Registers, a: Int, b: Int, c: Int -> s.put(c, if (s[a]!! == b) 1 else 0)},
        15 to {s: Registers, a: Int, b: Int, c: Int -> s.put(c, if (s[a]!! == s[b]!!) 1 else 0)}
    )

    fun solve_pt1(input: List<String>) : Int {
        val samples = parseSamples(input)
        val allPossible = samples.map { s -> ops.filterValues { it(s.before, s.a, s.b, s.c) == s.after }.size }
        return allPossible.filter { it >= 3 }.size
    }

    tailrec fun resolveOps(todo: List<Pair<Int, List<Int>>>, done: ImmutableMap<Int, (Registers, Int, Int, Int) -> Registers>) : ImmutableMap<Int, (Registers, Int, Int, Int) -> Registers>  {
        val curr = todo.sortedBy { it.second.size }.first()
        val thisOp = curr.second.single()
        val newDone = done.put(curr.first, ops[thisOp]!!)
        val newTodo = todo.minus(curr).map { it.first to it.second.filter { p -> p != thisOp } }
        return if (newTodo.isEmpty()) newDone
        else resolveOps(newTodo, newDone)
    }

    fun solve_pt2(samples: List<String>, program: List<String>) : Int {
        val samples = parseSamples(samples)
        val allPossible = samples.map { s -> s.op to ops.filterValues { it(s.before, s.a, s.b, s.c) == s.after }.keys }
        val sorted = allPossible.fold(immutableHashMapOf()) { acc: ImmutableMap<Int, List<Int>>, s: Pair<Int, Set<Int>> ->
            acc.put(s.first, acc.getOrDefault(s.first, emptyList()).plus(s.second).distinct())
        }.toList()
        val rOps = resolveOps(sorted, immutableHashMapOf())
        val prog = parseProgram(program)
        val endState = prog.fold(immutableHashMapOf(0 to 0, 1 to 0, 2 to 0, 3 to 0)) { state, instr ->
            rOps[instr.code]?.invoke(state, instr.a, instr.b, instr.c) ?: state
        }
        return endState[0] ?: -1
    }
}