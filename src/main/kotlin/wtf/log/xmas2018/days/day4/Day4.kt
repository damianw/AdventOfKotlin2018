package wtf.log.xmas2018.days.day4

import wtf.log.xmas2018.Solution
import wtf.log.xmas2018.Solver
import wtf.log.xmas2018.openResource
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// this is really bad
object Day4 : Solver<Int, Int> {

    private val timestampPattern = DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm")

    override fun solve(): Solution<Int, Int> {
        val input = processInput()
        return Solution(
                part1 = part1(input),
                part2 = part2(input)
        )
    }

    private fun part1(input: Map<Int, List<Shift>>): Int {
        val (guardId, shifts) = input.entries.maxBy { (_, shifts) ->
            shifts.sumBy { shift ->
                shift.asleepRanges.sumBy { it.duration.toMinutes().toInt() }
            }
        }!!

        val minuteCounts = mutableMapOf<Int, Int>()
        shifts.asSequence()
                .flatMap { it.asleepRanges.asSequence() }
                .flatMap { it.asSequence() }
                .map { it.minute }
                .forEach { minute ->
                    minuteCounts[minute] = (minuteCounts[minute] ?: 0) + 1
                }
        val maxMinute = minuteCounts.maxBy { it.value }!!.key
        return guardId * maxMinute
    }

    private fun part2(input: Map<Int, List<Shift>>): Int {
        val shiftRanges = input.values.asSequence().flatMap { shifts ->
            shifts.asSequence().map { it.range }
        }
        val earliest = shiftRanges.minBy { it.start }!!.start
        val latest = shiftRanges.maxBy { it.endInclusive }!!.endInclusive
        val range = TimeRange(earliest, latest)
        val minuteCounts = mutableMapOf<Int, MutableMap<Int, Int>>()
        range.asSequence().forEach { timestamp ->
            val counts = minuteCounts.getOrPut(timestamp.minute, ::mutableMapOf)
            input.values.asSequence()
                    .flatten()
                    .forEach { shift ->
                        if (shift.asleepRanges.any { timestamp in it }) {
                            counts[shift.guardId] = (counts[shift.guardId] ?: 0) + 1
                        }
                    }
        }
        val (maxMinute, counts) = minuteCounts.maxBy { (_, counts) -> counts.values.max() ?: 0 }!!
        val guardId = counts.maxBy { it.value }!!.key
        return maxMinute * guardId
    }

    private data class Shift(
            val guardId: Int,
            val range: TimeRange,
            val asleepRanges: List<TimeRange>
    )

    private data class TimeRange(
            override val start: LocalDateTime,
            val duration: Duration
    ) : ClosedRange<LocalDateTime> {

        constructor(
                start: LocalDateTime,
                endInclusive: LocalDateTime
        ) : this(
                start = start,
                duration = Duration.between(start, endInclusive.plusMinutes(1L))
        )

        override val endInclusive: LocalDateTime = start.plus(duration.minusMinutes(1L))

        fun asSequence(): Sequence<LocalDateTime> = sequence {
            for (i in 0 until duration.toMinutes()) {
                yield(start.plusMinutes(i))
            }
        }

    }

    private fun processInput(): Map<Int, List<Shift>> {
        val result = mutableMapOf<Int, MutableList<Shift>>()
        var timestamp: LocalDateTime? = null
        var currentGuardId: Int? = null
        var shiftStart: LocalDateTime? = null
        var asleepRanges = mutableListOf<TimeRange>()
        var fellAsleepAt: LocalDateTime? = null
        val lines = openResource("Day4.txt").use { it.readLines() }.sorted()
        for (line in lines) {
            timestamp = LocalDateTime.parse(line.substring(1, 17), timestampPattern)
            val eventPart = line.substring(19).split(' ')[1]
            when (eventPart.first()) {
                '#' -> {
                    check(fellAsleepAt == null)
                    if (currentGuardId != null) {
                        result.getOrPut(currentGuardId, ::mutableListOf).add(Shift(
                                guardId = currentGuardId,
                                range = TimeRange(
                                        start = shiftStart!!,
                                        duration = Duration.between(shiftStart, timestamp)
                                ),
                                asleepRanges = asleepRanges
                        ))
                    }
                    asleepRanges = mutableListOf()
                    currentGuardId = eventPart.substring(1).toInt()
                    shiftStart = timestamp
                }
                'a' -> {
                    check(currentGuardId != null)
                    check(shiftStart != null)
                    check(fellAsleepAt == null)
                    fellAsleepAt = timestamp
                }
                'u' -> {
                    check(currentGuardId != null)
                    check(shiftStart != null)
                    check(fellAsleepAt != null)
                    asleepRanges.add(TimeRange(
                            start = fellAsleepAt,
                            duration = Duration.between(fellAsleepAt, timestamp)
                    ))
                    fellAsleepAt = null
                }
                else -> throw AssertionError()
            }
        }
        if (currentGuardId != null) {
            result.getOrPut(currentGuardId, ::mutableListOf).add(Shift(
                    guardId = currentGuardId,
                    range = TimeRange(
                            start = shiftStart!!,
                            duration = Duration.between(shiftStart, timestamp)
                    ),
                    asleepRanges = asleepRanges
            ))
        }
        return result
    }

}
