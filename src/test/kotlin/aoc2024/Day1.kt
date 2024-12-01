package aoc2024

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.math.abs
import kotlin.test.Test
import kotlin.time.measureTime

class Day1 {
    @Test
    fun part1Example() {
        val input =
            $"""
            3   4
            4   3
            2   5
            1   3
            3   9
            3   3
            """.trimIndent()
                .lines()
        val result = part1(input)
        assertThat(result).isEqualTo(11)
    }

    @Test
    fun part1Input() {
        val lines = fileInput("Day1.txt").readLines()
        val duration = measureTime {
            val result = part1(lines)
            assertThat(result).isEqualTo(1889772)
        }
        println("part1 $duration")
    }

    fun part1(input: List<String>): Int {
        val listOfLists = input.map { str ->
            str.split("   ").map { it.toInt() }
        }.also(::println)

        val lefts = listOfLists.map { it.first() }
            .sorted()
            .apply(::println)

        val rights = listOfLists.map { it.last() }
            .sorted()
            .apply(::println)

        return lefts.zip(rights).sumOf { pair ->
            val dif = pair.second - pair.first
            if (dif < 0) {
                println("L: ${pair.first} R: ${pair.second} D: $dif")
            }
            abs(dif)
        }
    }
}
