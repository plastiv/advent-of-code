package aoc2024

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test
import kotlin.time.measureTime

class Day2 {
    @Test
    fun part1Example() {
        val input =
            $"""
            7 6 4 2 1
            1 2 7 8 9
            9 7 6 2 1
            1 3 2 4 5
            8 6 4 4 1
            1 3 6 7 9
            """.trimIndent()
                .lines()
        val result = part1(input)
        assertThat(result).isEqualTo(2)
    }

    @Test
    fun part1Input() {
        val lines = fileInput("Day2.txt").readLines()
        val duration = measureTime {
            val result = part1(lines)
            assertThat(result).isEqualTo(442)
        }
        println("part1 $duration")
    }

    fun part1(input: List<String>): Int {
        return input.map { str ->
            str.split(' ').filterNot(String::isEmpty).map(String::toInt)
        }
            .count { ints ->
                ints.areAscending() ||
                        ints.reversed().areAscending()
            }
    }

    fun List<Int>.areAscending(): Boolean = this.zipWithNext()
        .all { (first, second) -> (second - first) in 1..3 }

    @Test
    fun part2Example() {
        val input =
            $"""
            7 6 4 2 1
            1 2 7 8 9
            9 7 6 2 1
            1 3 2 4 5
            8 6 4 4 1
            1 3 6 7 9
            """.trimIndent()
                .lines()
        val result = part2(input)
        assertThat(result).isEqualTo(4)
    }

    @Test
    fun part2Input() {
        val lines = fileInput("Day2.txt").readLines()
        val duration = measureTime {
            val result = part2(lines)
            assertThat(result).isEqualTo(493)
        }
        println("part2 $duration")
    }

    fun part2(input: List<String>): Int {
        return input.map { str ->
            str.split(' ').filterNot(String::isEmpty).map(String::toInt)
        }
            .count { ints: List<Int> ->
                areAscending2(ints) || areAscending2(ints.reversed())
            }
    }

    private fun areAscending2(ints: List<Int>): Boolean {

        val find = ints.withIndex()
            .zipWithNext()
            .find { (first, second) ->
                (second.value - first.value) !in 1..3
            }

        if (find == null) {
            return true // all valid
        }

        val (first, second) = find
        return ints.removeIndex(first.index).areAscending() || ints.removeIndex(second.index).areAscending()
    }

    fun List<Int>.removeIndex(index: Int): List<Int> {
        return subList(0, index) + subList(index + 1, size)
    }
}
