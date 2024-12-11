package aoc2024

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test
import kotlin.time.measureTime

class Day11 {
    @Test
    fun part1Example() {
        val input =
            $"""
            125 17
            """.trimIndent()
                .lines()
        val result = part1(input)
        assertThat(result).isEqualTo(55312)
    }

    @Test
    fun part1Input() {
        val lines = fileInput("Day11.txt").readLines()
        val duration = measureTime {
            val result = part1(lines)
            assertThat(result).isEqualTo(220722)
        }
        println("part1 $duration")
    }

    fun part1(input: List<String>): Long {
        val stones = input.first().split(' ').map(String::toLong)
        return stones.sumOf { blink(it, 24) }
    }

    @Test
    fun part2Example() {
        val input =
            $"""
            125 17
            """.trimIndent()
                .lines()
        assertThat(this.part2(input, 5)).isEqualTo(22)
        assertThat(this.part2(input, 24)).isEqualTo(55312)
    }

    @Test
    fun part2ExampleSteps() {
//        Initial arrangement:
//        125 17
//        After 1 blink:
//        253000 1 7
//        After 2 blinks:
//        253 0 2024 14168
//        After 3 blinks:
//        512072 1 20 24 28676032
//        After 4 blinks:
//        512 72 2024 2 0 2 4 2867 6032
//        After 5 blinks:
//        1036288 7 2 20 24 4048 1 4048 8096 28 67 60 32
//        After 6 blinks:
//        2097446912 14168 4048 2 0 2 4 40 48 2024 40 48 80 96 2 8 6 7 6 0 3 2
        assertThat(blink(125, 0)).isEqualTo(1) // 1 iteration
        assertThat(blink(125, 1)).isEqualTo(2) // 2 iterations
        assertThat(blink(125, 2)).isEqualTo(2) // 3 iterations
        assertThat(blink(125, 3)).isEqualTo(3) // 4 iterations
        assertThat(blink(125, 4)).isEqualTo(5) // 5 iterations
        assertThat(blink(125, 5)).isEqualTo(7) // 6 iterations
    }

    @Test
    fun part2Input() {
        val lines = fileInput("Day11.txt").readLines()
        val duration = measureTime {
            val result = part2(lines, 74)
            assertThat(result).isEqualTo(261952051690787)
        }
        println("part2 $duration")
    }

    val cache = mutableMapOf<Pair<Long, Int>, Long>()

    fun blink(number: Long, times: Int): Long {
        return cache.getOrPut(number to times) {
            val numberStr = number.toString()
            if (times == 0) { // last round
                when {
                    number == 0L -> 1
                    numberStr.length % 2 == 0 -> 2
                    else -> 1
                }
            } else {
                when {
                    number == 0L -> blink(1, times.dec())
                    numberStr.length % 2 == 0 -> {
                        val left = numberStr.substring(0, numberStr.length / 2).toLong()
                        val right = numberStr.substring(numberStr.length / 2, numberStr.length).toLong()
                        blink(left, times.dec()) + blink(right, times.dec())
                    }

                    else -> blink(number * 2024, times.dec())
                }
            }
        }
    }

    fun part2(input: List<String>, maxIter: Int): Long {
        val stones = input.first().split(' ').map(String::toLong)

        return stones.sumOf { blink(it, maxIter) }
    }
}
