import kotlinx.collections.immutable.*

object Day8 {

    data class TreeNode(val children: ImmutableList<TreeNode>, val metadata: ImmutableList<Int>) {
        fun sumMetadata() : Int = metadata.sum() + children.fold(0) {acc, c -> acc + c.sumMetadata()}

        fun value() : Int =
            if (children.isEmpty()) metadata.sum() else metadata.fold(0) {acc, i ->
                if (children.size >= i) acc + children[i-1].value() else acc
            }
    }

    fun parseNode(input: Iterator<String>) : TreeNode {
        val childCount = input.next().toInt()
        val metadataCount = input.next().toInt()
        val children = (0 until childCount).fold(immutableListOf<TreeNode>()) { acc, _ -> acc + parseNode(input)}
        val metadata = (0 until metadataCount).fold(immutableListOf<Int>()) { acc, _ -> acc + input.next().toInt() }
        return TreeNode(children, metadata)
    }

    fun solve_pt1(input: String) : Int = parseNode(input.splitToSequence(" ").iterator()).sumMetadata()

    fun solve_pt2(input: String) : Int = parseNode(input.splitToSequence(" ").iterator()).value()
}