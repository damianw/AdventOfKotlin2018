package wtf.log.xmas2018.days.day3

import wtf.log.xmas2018.Solution
import wtf.log.xmas2018.Solver
import wtf.log.xmas2018.openResource

object Day3 : Solver<Int, Int> {

    override fun solve(): Solution<Int, Int> {
        val claims = readInput()
        val table = mutableMapOf<Point, Int>()
        for (claim in claims) {
            table.intersect(claim.rect)
        }
        return Solution(
                part1 = table.values.count { it >= 2 },
                part2 = claims.single { table.isExclusive(it.rect) }.id
        )
    }

    private fun readInput(): List<Claim> = openResource("Day3.txt").useLines { lines ->
        lines.map((Claim)::parse).toList()
    }

    private fun MutableMap<Point, Int>.intersect(rect: Rect) {
        for (point in rect.coordinates) {
            this[point] = (this[point] ?: 0) + 1
        }
    }

    private fun Map<Point, Int>.isExclusive(rect: Rect): Boolean {
        return rect.coordinates.all { get(it) == 1 }
    }

    private data class Point(val x: Int, val y: Int)

    private data class Rect(
            val left: Int,
            val top: Int,
            val width: Int,
            val height: Int
    ) {

        val right: Int
            get() = left + width
        val bottom: Int
            get() = top + height

        val coordinates: Sequence<Point>
            get() = (left until right).asSequence().flatMap { x ->
                (top until bottom).asSequence().map { y -> Point(x, y) }
            }


    }

    private data class Claim(
            val id: Int,
            val rect: Rect
    ) {

        companion object {

            private val PATTERN = Regex("""#(\d+) @ (\d+),(\d+): (\d+)x(\d+)""")

            fun parse(input: String): Claim {
                val (id, left, top, width, height) = PATTERN.matchEntire(input)!!.destructured
                return Claim(
                        id = id.toInt(),
                        rect = Rect(
                                left = left.toInt(),
                                top = top.toInt(),
                                width = width.toInt(),
                                height = height.toInt()
                        )
                )
            }

        }

    }

}
