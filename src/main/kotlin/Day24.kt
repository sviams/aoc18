import kotlinx.collections.immutable.*

object Day24 {

    enum class DamageType { SLASHING, RADIATION, FIRE, BLUDGEONING, COLD }

    data class Group(val units: Int, val hp: Int, val weaknesses: List<DamageType>, val immunities: List<DamageType>, val attackType: DamageType, val attackStrength: Int, val initiative: Int, val id: String) {
        fun effectivePower() = units * attackStrength
        fun damageTo(other: Group) : Int {
            return when {
                other.weaknesses.contains(attackType) -> effectivePower() * 2
                other.immunities.contains(attackType) -> 0
                else -> effectivePower()
            }
        }

        fun tryKill(o: Group) : Group {
            val dmg = damageTo(o)
            val unitsToLose = dmg / o.hp
            val actualUnitsLost = if (o.units < unitsToLose) o.units else unitsToLose
            return Group(o.units - actualUnitsLost, o.hp, o.weaknesses, o.immunities, o.attackType, o.attackStrength, o.initiative, o.id)
        }

        override fun toString(): String = id

        override fun equals(other: Any?): Boolean = (other as Group).id == id

        override fun hashCode(): Int = id.hashCode()
    }

    fun parseInput(input: List<String>, prefix: String, boost: Int) : List<Group> {
        return input.foldIndexed(emptyList<Group>()) { index, acc, line ->
            val parts = line.split(" hit points", "with an attack that does ")
            val (units,hp) = """(\d+) units each with (\d+)""".toRegex().find(parts[0])!!.destructured
            val (ap,type,init) = """(\d+) (\S+) damage at initiative (\d+)""".toRegex().find(parts[2])!!.destructured
            val dt = DamageType.valueOf(type.toUpperCase())
            val modParts = parts[1].trim().trimEnd().split(";")
            val immunes = modParts.singleOrNull { it.contains("immune to") }?.substringAfter("immune to")?.trim(')')?.split(',')?.map { DamageType.valueOf(it.trimStart().toUpperCase()) }
            val weaks = modParts.singleOrNull { it.contains("weak to") }?.substringAfter("weak to")?.trim(')')?.split(',')?.map { DamageType.valueOf(it.trimStart().toUpperCase()) }
            acc + Group(units.toInt(),hp.toInt(), weaks ?: emptyList(), immunes ?: emptyList(), dt, ap.toInt() + boost, init.toInt(), "$prefix$index")
        }

    }

    tailrec fun selectTargets(attackers: ImmutableList<Group>, defenders: ImmutableList<Group>, result: ImmutableMap<Group, Group>) : ImmutableMap<Group, Group> {
        val attacker = attackers.sortedWith(compareBy({it.effectivePower()}, {it.initiative})).last()
        val bestTarget = defenders.sortedWith(compareBy({attacker.damageTo(it)}, {it.effectivePower()}, {it.initiative})).last()
        val newAttackers = attackers.minus(attacker)
        val newDefenders = if (attacker.damageTo(bestTarget) > 0) defenders.minus(bestTarget) else defenders
        val newResult = if (attacker.damageTo(bestTarget) > 0) result.put(attacker, bestTarget) else result
        return if (newAttackers.isEmpty() || newDefenders.isEmpty()) newResult
        else selectTargets(newAttackers, newDefenders, newResult)
    }

    tailrec fun attackTargets(targets: ImmutableList<Pair<Group, Group>>, allImms: ImmutableList<Group>, allInfs: ImmutableList<Group>, killed: ImmutableList<Group>) : Pair<ImmutableList<Group>, ImmutableList<Group>> {
        if (targets.isEmpty()) return allImms to allInfs
        val all = allImms.plus(allInfs)
        val (a,d) = targets.first()
        val attacker = all.single { it == a } // Refreshed from current state
        val defender = all.single { it == d }
        val newTargets = targets.removeAt(0)
        val newVictim = attacker.tryKill(defender)
        val newKilled = if (newVictim.units <= 0) killed.plus(newVictim) else killed
        val newImms = if (newVictim.units > 0 && allImms.contains(defender)) allImms.minus(defender).plus(newVictim) else allImms.minus(defender)
        val newInfs = if (newVictim.units > 0 && allInfs.contains(defender)) allInfs.minus(defender).plus(newVictim) else allInfs.minus(defender)
        val culledTargets = if (newVictim.units <= 0) newTargets.filter { it.first != defender && it.second != defender }.toImmutableList() else newTargets
        return if (culledTargets.isEmpty()) return newImms to newInfs
        else attackTargets(culledTargets, newImms, newInfs, newKilled)
    }

    tailrec fun fightTilDeath(imms: ImmutableList<Group>, infs: ImmutableList<Group>) : Pair<ImmutableList<Group>, ImmutableList<Group>> {
        val immTargets = selectTargets(imms, infs, immutableHashMapOf()).toList()
        val infTargets = selectTargets(infs, imms, immutableHashMapOf()).toList()
        val allTargets = immTargets.plus(infTargets).sortedByDescending { it.first.initiative }.toImmutableList()
        val (newImms, newInfs) = attackTargets(allTargets, imms, infs, immutableListOf())
        return if (newImms.sumBy { it.units } == imms.sumBy { it.units } && newInfs.sumBy { it.units } == infs.sumBy { it.units }) immutableListOf<Group>() to immutableListOf() // Stalemate
        else if (newImms.isEmpty() || newInfs.isEmpty()) newImms to newInfs
        else fightTilDeath(newImms, newInfs)
    }

    fun solve_pt1(input: List<String>) : Int {
        val immunes = parseInput(input.drop(1).takeWhile { it != "Infection:" }.filterNot { it.isEmpty() }, "D", 0).toImmutableList()
        val infections = parseInput(input.takeLastWhile { it != "Infection:" }.filterNot { it.isEmpty() }, "A", 0).toImmutableList()
        val (endImms, endInfs) = fightTilDeath(immunes, infections)
        return endImms.sumBy { it.units } + endInfs.sumBy { it.units }
    }

    fun do_solve_pt2(input: List<String>, boost: Int) : Int {
        val immunes = parseInput(input.drop(1).takeWhile { it != "Infection:" }.filterNot { it.isEmpty() }, "D", boost).toImmutableList()
        val infections = parseInput(input.takeLastWhile { it != "Infection:" }.filterNot { it.isEmpty() }, "A", 0).toImmutableList()
        val (endImms, endInfs) = fightTilDeath(immunes, infections)
        return endImms.sumBy { it.units }
    }

    fun solve_pt2(input: List<String>) : Int {
        (40 .. 60).forEach {
            val res = do_solve_pt2(input, it)
            if (res > 0) return res
        }
        return 0
    }
}