package aoc2023

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.collections.flatten
import kotlin.test.Test
import kotlin.time.measureTime

class Day6 {
    @Test
    fun part1Example() {
        val input =
            $"""
            Time:      7  15   30
            Distance:  9  40  200
            """.trimIndent()
                .lines()
        val result = part1(input)
        assertThat(result).isEqualTo(288)
    }

    @Test
    fun part1Input() {
        val lines = fileInput("Day6.txt").readLines()
        val duration = measureTime {
            val result = part1(lines)
            assertThat(result).isEqualTo(588588)
        }
        println("part1 $duration")
    }

    private fun part1(input: List<String>): Int {
        return input.map { str ->
            str.substringAfter(':')
                .split(' ')
                .filterNot { it.isEmpty() }
                .map { str -> str.toInt() }
        }.zipWithNext { a, b -> a.zip(b) }
            .flatten()
            .also(::println)
            .map { (raceTime, distanceRecord) ->
                (0..raceTime).map { holdTime ->
                    // what is resulted distance for such hold time?
                    // initial speed * rest of the time
                    holdTime * (raceTime - holdTime)
                }.count { travelDistance -> travelDistance > distanceRecord }
            }.reduce { acc, i -> acc * i }
    }

    @Test
    fun part2Example() {
        val input =
            $"""
            Time:      7  15   30
            Distance:  9  40  200
            """.trimIndent()
                .lines()
        val result = part2(input)
        assertThat(result).isEqualTo(71503)
    }

    @Test
    fun part2Input() {
        val lines = fileInput("Day6.txt").readLines()
        val duration = measureTime {
            val result = part2(lines)
            assertThat(result).isEqualTo(34655848)
        }
        println("part2 $duration")
    }

    fun part2(input: List<String>): Long {
        val (raceTime, distanceRecord) = input.map { str ->
            str.substringAfter(':')
                .filter(Char::isDigit)
                .toLong()
        }.zipWithNext()
            .first()

        val lowestHoldTimeToBeatTheRecord = lowerBinarySearch(raceTime) { holdTime ->
            val travelDistance = holdTime * (raceTime - holdTime)
            travelDistance > distanceRecord
        }
        val highestHoldTimeToBeatTheRecord = lowerBinarySearch(raceTime) { holdTime ->
            val travelDistance = holdTime * (raceTime - holdTime)
            // flip the condition to find first highest value
            travelDistance < distanceRecord
        }
        return highestHoldTimeToBeatTheRecord - lowestHoldTimeToBeatTheRecord
    }

    fun lowerBinarySearch(raceTime: Long, ok: (Long) -> Boolean ): Long {
        var l = -1L
        var r = raceTime
        while (r - l > 1) {
            val holdTime = (l + r) / 2
            if (ok(holdTime)) {
                r = holdTime
            } else {
                l = holdTime
            }
        }
        return r
    }
}
