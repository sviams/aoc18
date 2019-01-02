import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableHashMap

object Day3 {

    data class Claim(val id: Int, val left: Int, val top: Int, val w: Int, val h: Int) {

        val rows = top until (h + top)
        val cols = left until (w + left)

        fun isFree(cloth: ImmutableMap<String, Int>) : Boolean {
            rows.forEach { row -> cols.forEach { col -> if (cloth["$row col $col"]!! > 1) return false } }
            return true
        }
    }

    fun parseClaim(row: String) : Claim {
        val (id, l, t, w, h) = """#(\d+) @ (\d+),(\d+): (\d+)x(\d+)""".toRegex().find(row)!!.destructured
        return Claim(id.toInt(), l.toInt(),t.toInt(),w.toInt(),h.toInt())
    }

    fun cleanSheet() = emptyMap<String, Int>().toImmutableHashMap()

    fun claimsToCloth(claims: List<Claim>) : ImmutableMap<String, Int> =
        claims.fold(cleanSheet()) { cloth, claim ->
            cloth.putAll(claim.rows.fold(cleanSheet()) { row, y ->
                row.putAll(claim.cols.fold(row) {col, x ->
                    val key = "$y col $x"
                    val existing = cloth[key] ?: 0
                    col.put(key, existing+1 )
                })
            })
        }

    fun solve_pt1(input: List<String>) : Int {
        val claims = input.map { parseClaim(it) }
        val cloth = claimsToCloth(claims)
        return cloth.count { it.value > 1 }
    }

    fun solve_pt2(input: List<String>) : Int {
        val claims = input.map { parseClaim(it) }
        val cloth = claimsToCloth(claims)
        return claims.takeWhileInclusive { !it.isFree(cloth) }.last().id
    }

}