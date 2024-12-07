package aoc2024

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test
import kotlin.time.measureTime

class Day7 {
    @Test
    fun part1Example() {
        val input =
            $"""
            190: 10 19
            3267: 81 40 27
            83: 17 5
            156: 15 6
            7290: 6 8 6 15
            161011: 16 10 13
            192: 17 8 14
            21037: 9 7 18 13
            292: 11 6 16 20
            """.trimIndent()
                .lines()
        val result = part1(input)
        assertThat(result).isEqualTo(3749)
    }

    @Test
    fun part1Input() {
        val lines = fileInput("Day7.txt").readLines()
        val duration = measureTime {
            val result = part1(lines)
            assertThat(result).isEqualTo(303876485655)
        }
        println("part1 $duration")
    }

    fun evaluate(target: Long, numbers: List<Long>, equations: List<(Long, Long) -> Long>): Boolean {
        fun evaluate(cur: Long, index: Int): Boolean = when {
            // optimization
            cur > target -> false
            numbers.size == index -> cur == target
            // left to right
            else -> equations.any { equation -> evaluate(equation(cur, numbers[index]), index + 1) }
        }
        return evaluate(numbers[0], 1)
    }

    fun part1(input: List<String>): Long {
        return input.map { str ->
            val operator = str.substringBefore(':').toLong()
            val eq = str.substringAfter(':').split(' ').filter { str -> str.isNotEmpty() }.map { str -> str.toLong() }
            Pair(operator, eq)
        }.also(::println)
            .filter { (operator, eq) ->
                // can operator be found

                evaluate(operator, eq, listOf(Long::plus, Long::times))
            }.sumOf { it.first }

    }

    @Test
    fun part2Example() {
        val input =
            $"""
            190: 10 19
            3267: 81 40 27
            83: 17 5
            156: 15 6
            7290: 6 8 6 15
            161011: 16 10 13
            192: 17 8 14
            21037: 9 7 18 13
            292: 11 6 16 20
            """.trimIndent()
                .lines()
        val result = part2(input)
        assertThat(result).isEqualTo(11387)
    }

    @Test
    fun part2Input() {
        val lines = fileInput("Day7.txt").readLines()
        val duration = measureTime {
            val result = part2(lines)
            assertThat(result).isEqualTo(146111650210682)
        }
        println("part2 $duration")
    }

    fun part2(input: List<String>): Long {
        return input.map { str ->
            val operator = str.substringBefore(':').toLong()
            val eq = str.substringAfter(':').split(' ').filter { str -> str.isNotEmpty() }.map { str -> str.toLong() }
            Pair(operator, eq)
        }.also(::println)
            .filter { (operator, eq) ->
                // can operator be found

                evaluate(operator, eq, listOf(Long::plus, Long::times, { first, second -> "$first$second".toLong() }))
            }.sumOf { it.first }
    }
}
