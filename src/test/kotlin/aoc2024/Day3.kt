package aoc2024

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test
import kotlin.time.measureTime

class Day3 {
    @Test
    fun part1Example() {
        val input =
            $"""
            from()why()?mul(603,692)({select()}] )]-(mul(387,685)who()mul(28,717)who()]
            """.trimIndent()
                .lines()
        val result = part1(input)
        assertThat(result).isEqualTo(603*692 + 387*685 + 28*717)
    }

    @Test
    fun part1Input() {
        val lines = fileInput("Day3.txt").readLines()
        val duration = measureTime {
            val result = part1(lines)
            assertThat(result).isEqualTo(442)
        }
        println("part1 $duration")
    }

    fun part1(input: List<String>): Int {
        val regex = "mul\\(\\d+,\\d+\\)".toRegex()
        return input.sumOf { str ->
            regex.findAll(str).sumOf { result ->
                val twoNum= result.groupValues[0]
                val first = twoNum.substringAfter("mul(").substringBefore(",")
                val second = twoNum.substringAfter(",").substringBefore(")")
                first.toInt() * second.toInt()
            }
        }
    }

    @Test
    fun part2Input() {
        val lines = fileInput("Day3.txt").readLines()
        val duration = measureTime {
            val result = part2(lines)
            assertThat(result).isEqualTo(493)
        }
        println("part2 $duration")
    }

    fun part2(input: List<String>): Int {
        val regex = "mul\\(\\d+,\\d+\\)|(don't\\(\\))|(do\\(\\))".toRegex()
        var doCount = true
        return input.sumOf { str ->
            regex.findAll(str).sumOf { result ->
                val match= result.groupValues[0]
                when (match) {
                    "do()" -> {
                        doCount = true
                        0
                    }
                    "don't()" -> {
                        doCount = false
                        0
                    }
                    else -> if (doCount) {
                        val first = match.substringAfter("mul(").substringBefore(",")
                        val second = match.substringAfter(",").substringBefore(")")
                        first.toInt() * second.toInt()
                    } else {
                        0
                    }
                }

            }
        }
    }
}
