package wtf.log.xmas2018.days.day5

import wtf.log.xmas2018.Solution
import wtf.log.xmas2018.Solver
import wtf.log.xmas2018.openResource

object Day5 : Solver<Int, Int> {

    override fun solve(): Solution<Int, Int> {
        val input = readInput()
        return Solution(
                part1 = input.reduceFully().length,
                part2 = part2(input)
        )
    }

    private fun part2(input: String): Int {
        return ('a'..'z')
                .asSequence()
                .map { letter ->
                    input.filter { it != letter && it != letter.toUpperCase() }.reduceFully().length
                }
                .min()!!
    }

    private fun String.reduceFully(): String {
        val buffer = StringBuilder(this)
        while (buffer.reduce());
        return buffer.toString()
    }

    private fun readInput(): String = openResource("Day5.txt").use { it.readLine() }

    private fun Char.oppositeCase(): Char = when {
        isUpperCase() -> toLowerCase()
        isLowerCase() -> toUpperCase()
        else -> 0.toChar()
    }

    private fun StringBuilder.reduce(): Boolean {
        // go backwards to optimize array copying
        val initialLength = length
        var index = lastIndex - 1
        var nextChar: Char = get(lastIndex)
        while (index >= 0) {
            val currentChar = get(index)
            if (currentChar.oppositeCase() == nextChar) {
                delete(index, index + 2)
                if (index == 0) break
                index--
                nextChar = get(index)
                index--
            } else {
                nextChar = currentChar
                index--
            }
        }
        return length != initialLength
    }

}
