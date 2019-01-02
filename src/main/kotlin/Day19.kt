import kotlinx.collections.immutable.*

object Day19 {

    data class Op(val code: String, val a: Int, val b: Int, val c: Int) {
        override fun toString(): String = "$code $a $b $c"
    }

    fun parseProgram(input: List<String>) : ImmutableList<Op> =
        input.fold(immutableListOf()) { acc, line ->
            val (op, a, b, c) = """(\S+) (\S+) (\S+) (\S+)""".toRegex().find(line)!!.destructured
            acc + Op(op, a.toInt(), b.toInt(), c.toInt())
        }

    val ops = immutableHashMapOf(
        "addr" to {s: Registers, a: Int, b: Int, c: Int -> s.put(c, s[a]!! + s[b]!!)},
        "addi" to {s: Registers, a: Int, b: Int, c: Int -> s.put(c, s[a]!! + b)},
        "mulr" to {s: Registers, a: Int, b: Int, c: Int -> s.put(c, s[a]!! * s[b]!!)},
        "muli" to {s: Registers, a: Int, b: Int, c: Int -> s.put(c, s[a]!! * b)},
        "banr" to {s: Registers, a: Int, b: Int, c: Int -> s.put(c, s[a]!! and s[b]!!)},
        "bani" to {s: Registers, a: Int, b: Int, c: Int -> s.put(c, s[a]!! and b)},
        "borr" to {s: Registers, a: Int, b: Int, c: Int -> s.put(c, s[a]!! or s[b]!!)},
        "bori" to {s: Registers, a: Int, b: Int, c: Int -> s.put(c, s[a]!! or b)},
        "setr" to {s: Registers, a: Int, b: Int, c: Int -> s.put(c, s[a]!!)},
        "seti" to {s: Registers, a: Int, b: Int, c: Int -> s.put(c, a)},
        "gtir" to {s: Registers, a: Int, b: Int, c: Int -> s.put(c, if (a > s[b]!!) 1 else 0)},
        "gtri" to {s: Registers, a: Int, b: Int, c: Int -> s.put(c, if (s[a]!! > b) 1 else 0)},
        "gtrr" to {s: Registers, a: Int, b: Int, c: Int -> s.put(c, if (s[a]!! > s[b]!!) 1 else 0)},
        "eqir" to {s: Registers, a: Int, b: Int, c: Int -> s.put(c, if (a == s[b]!!) 1 else 0)},
        "eqri" to {s: Registers, a: Int, b: Int, c: Int -> s.put(c, if (s[a]!! == b) 1 else 0)},
        "eqrr" to {s: Registers, a: Int, b: Int, c: Int -> s.put(c, if (s[a]!! == s[b]!!) 1 else 0)}
    )

    tailrec fun execute(state: Registers, program: ImmutableList<Op>, ip: Int, ipBinding: Int) : Registers {
        if (ip >= program.size) return state
        val nextOp = program[ip]
        val nextState = ops[nextOp.code]?.invoke(state.put(ipBinding, ip), nextOp.a, nextOp.b, nextOp.c) ?: state
        return execute(nextState, program, nextState[ipBinding]!! + 1, ipBinding)
    }

    fun solve_pt1(input: List<String>) : Int {
        val (ipBinding) = """#ip (\d)""".toRegex().find(input.first())!!.destructured
        val program = parseProgram(input.drop(1))
        val startState = immutableHashMapOf(0 to 0, 1 to 0, 2 to 0, 3 to 0, 4 to 0, 5 to 0)
        val endState = execute(startState, program, 0, ipBinding.toInt())
        return endState[0]!!
    }

    fun solve_pt2(input: List<String>) : Int {
        val r2 = 10551377 // Register 2 from running the program a while with reg 0 set to 1
        return r2 + (1 .. r2/2).filter { r2 % it == 0 }.sum()
    }
}