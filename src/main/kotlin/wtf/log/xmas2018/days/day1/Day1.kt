package wtf.log.xmas2018.days.day1

import wtf.log.xmas2018.Solution
import wtf.log.xmas2018.Solver
import wtf.log.xmas2018.openResource

object Day1 : Solver<Int, Int> {

    override fun solve(): Solution<Int, Int> {
        val input = openResource("Day1.txt").useLines { lines ->
            lines.map(String::toInt).toList()
        }
        return Solution(
                part1 = input.sum(),
                part2 = part2(input)
        )
    }

    private fun part2(input: List<Int>): Int {
        var frequency = 0
        val seen = mutableMapOf<Int, Boolean>()
        seen[0] = true
        val infiniteSequence = generateSequence { input }.flatten()
        for (value in infiniteSequence) {
            frequency += value
            if (seen[frequency] == true) {
                return frequency
            }
            seen[frequency] = true
        }
        println(frequency)
        throw AssertionError()
    }

}
