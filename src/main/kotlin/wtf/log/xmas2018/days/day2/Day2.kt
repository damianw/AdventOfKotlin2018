package wtf.log.xmas2018.days.day2

import com.google.common.collect.Sets
import wtf.log.xmas2018.Solution
import wtf.log.xmas2018.Solver
import wtf.log.xmas2018.openResource

@ExperimentalUnsignedTypes
object Day2 : Solver<UInt, String> {

    override fun solve(): Solution<UInt, String> {
        val input = openResource("Day2.txt").use { it.readLines() }.toSet()
        return Solution(
                part1 = part1(input),
                part2 = part2(input)
        )
    }

    private fun part1(input: Set<String>): UInt {
        var twoCount = 0u
        var threeCount = 0u
        for (value in input) {
            val countValues = value.counts().values
            if (countValues.any { it == 2u }) {
                twoCount += 1u
            }
            if (countValues.any { it == 3u }) {
                threeCount += 1u
            }
        }
        return twoCount * threeCount
    }

    private fun String.counts(): Map<Char, UInt> {
        val result = mutableMapOf<Char, UInt>()
        forEach { char ->
            result[char] = (result[char] ?: 0u) + 1u
        }
        return result
    }

    // yeah I borrowed this from Wikipedia... it's a solved problem
    private fun levenshtein(lhs: CharSequence, rhs: CharSequence): Int {
        val lhsLength = lhs.length
        val rhsLength = rhs.length

        var cost = IntArray(lhsLength + 1) { it }
        var newCost = IntArray(lhsLength + 1) { 0 }

        for (i in 1..rhsLength) {
            newCost[0] = i

            for (j in 1..lhsLength) {
                val editCost = if (lhs[j - 1] == rhs[i - 1]) 0 else 1

                val costReplace = cost[j - 1] + editCost
                val costInsert = cost[j] + 1
                val costDelete = newCost[j - 1] + 1

                newCost[j] = minOf(costInsert, costDelete, costReplace)
            }

            val swap = cost
            cost = newCost
            newCost = swap
        }

        return cost[lhsLength]
    }

    private fun part2(input: Set<String>): String {
        val (a, b) = Sets.cartesianProduct(input, input).first { (a, b) -> levenshtein(a, b) == 1 }
        var index = 0
        while (a[index] == b[index]) {
            index += 1
        }
        return "${a.substring(0, index)}${b.substring(index + 1)}"
    }

}
