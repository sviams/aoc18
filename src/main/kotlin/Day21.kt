import kotlinx.collections.immutable.*

typealias Instruction = (Registers) -> Registers

object Day21 {

    val EQRR = 28

    fun parseInstructions(input: List<String>, ipBinding: Int) : ImmutableList<Instruction> =
        input.fold(immutableListOf()) { acc, line ->
            val (op, a, b, c) = """(\S+) (\S+) (\S+) (\S+)""".toRegex().find(line)!!.destructured
            acc + instructionLambda(op, a.toInt(), b.toInt(), c.toInt(), ipBinding)
        }

    fun instructionLambda(code: String, a: Int, b: Int, c: Int, ipBinding: Int) : (Registers) -> Registers {
        val opFunc = ops[code]!!
        return { s: Registers -> incIp(opFunc(s,a,b,c), ipBinding) }
    }

    fun incIp(s: Registers, ipBinding: Int) : Registers = s.put(ipBinding, s[ipBinding]!! + 1)

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

    tailrec fun executePt1(state: Registers, program: ImmutableList<Instruction>, ipBinding: Int) : Registers {
        val ip = state[ipBinding]!!
        val nextState = program[ip](state)
        if (ip == EQRR) return nextState
        return executePt1(nextState, program, ipBinding)
    }

    tailrec fun executePt2(state: Registers, program: ImmutableList<Instruction>, ipBinding: Int, threes: ImmutableList<Int>) : Int {
        val ip = state[ipBinding]!!
        val nextState = program[ip](state)
        if (ip == EQRR && threes.contains(nextState[3]!!)) return threes.last()
        if (ip == EQRR && threes.size % 100 == 0) println("${threes.size} -> $state -> $nextState")
        return executePt2(nextState, program, ipBinding, if (ip == EQRR) threes.plus(nextState[3]!!) else threes)
    }

    fun solve_pt1(input: List<String>) : Int {
        val (ipBinding) = """#ip (\d)""".toRegex().find(input.first())!!.destructured
        val program = parseInstructions(input.drop(1), ipBinding.toInt())
        val startState = immutableHashMapOf(0 to 0, 1 to 0, 2 to 0, 3 to 0, 4 to 0, 5 to 0)
        val endState = executePt1(startState, program, ipBinding.toInt())
        return endState[3]!!
    }

    fun solve_pt2(input: List<String>) : Int {
        val (ipBinding) = """#ip (\d)""".toRegex().find(input.first())!!.destructured
        val program = parseInstructions(input.drop(1), ipBinding.toInt())
        val startState = immutableHashMapOf(0 to 0, 1 to 0, 2 to 0, 3 to 0, 4 to 0, 5 to 0)
        val endState = executePt2(startState, program, ipBinding.toInt(), immutableListOf())
        return endState
    }
}