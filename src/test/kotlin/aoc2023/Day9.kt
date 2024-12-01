package aoc2023

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test
import kotlin.time.measureTime

class Day9 {
    @Test
    fun part1Example() {
        val input =
            $"""
            0 3 6 9 12 15
            1 3 6 10 15 21
            10 13 16 21 30 45
            """.trimIndent()
                .lines()
        val result = part1(input)
        assertThat(result).isEqualTo(114)
    }

    @Test
    fun part1Input() {
        val lines = fileInput("Day9.txt").readLines()
        val duration = measureTime {
            val result = part1(lines)
            assertThat(result).isEqualTo(2005352194)
        }
        println("part1 $duration")
    }

    fun part1(input: List<String>): Int {
        return input.map { str ->
            str.split(' ').map { it.toInt() }
        }.sumOf { ints ->
            predictNextValue(records = ints)
        }
    }

    fun predictNextValue(records: List<Int>): Int {
        if (records.all { it == 0 }) return 0

        val newList = records.windowed(2)
            .map { (first, second) -> second - first }
        return records.last() + predictNextValue(newList)
    }
}
