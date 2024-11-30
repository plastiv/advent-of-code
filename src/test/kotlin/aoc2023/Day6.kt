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
}
