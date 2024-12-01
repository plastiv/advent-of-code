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
        val (lefts, rights) = input.map { str ->
            str.split("   ")
                .map(String::toInt)
                .let { it.first() to it.last() }
        }.unzip()

        return lefts
            .sorted()
            .zip(rights.sorted())
            .sumOf { pair ->
                abs(pair.second - pair.first)
            }
    }

    @Test
    fun part2Example() {
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
        val result = part2(input)
        assertThat(result).isEqualTo(31)
    }

    @Test
    fun part2Input() {
        val lines = fileInput("Day1.txt").readLines()
        val duration = measureTime {
            val result = part2(lines)
            assertThat(result).isEqualTo(23228917)
        }
        println("part1 $duration")
    }

    fun part2(input: List<String>): Int {
        val (lefts, rights) = input.map { str ->
            str.split("   ")
                .map(String::toInt)
                .let { it.first() to it.last() }
        }.unzip()

        return lefts.sumOf { left ->
            left * rights.count { right -> right == left }
        }
    }
}
