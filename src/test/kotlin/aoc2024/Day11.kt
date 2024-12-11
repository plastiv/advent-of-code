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

    fun part1(input: List<String>): Int {
        val stones = input.first().split(' ').map(String::toInt)
        var currentStones = stones.map { it.toLong() }
        repeat(25) {
            val newList = buildList {
                currentStones.forEach {
                    if (it == 0L) {
                        add(1L)
                    } else if (it.toString().length % 2 == 0) {
                        val str = it.toString()
                        add(str.substring(0, str.length / 2).toLong())
                        add(str.substring(str.length / 2, str.length).toLong())
                    } else {
                        add(it * 2024)
                    }
                }
            }
            currentStones = newList
        }
        return currentStones.size
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
        assertThat(count(0, 125, 1)).isEqualTo(1) // 1 iteration
        assertThat(count(1, 125, 1)).isEqualTo(2) // 2 iterations
        assertThat(count(2, 125, 1)).isEqualTo(2) // 3 iterations
        assertThat(count(3, 125, 1)).isEqualTo(3) // 4 iterations
        assertThat(count(4, 125, 1)).isEqualTo(5) // 5 iterations
        assertThat(count(5, 125, 1)).isEqualTo(7) // 6 iterations
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
    tailrec fun count(iter: Int, number: Long, acc: Long): Long {
        val key = Pair(number, iter)
        val hit = cache[key]
        if (hit != null) {
            return hit
        } else {

            val numberStr = number.toString()
            return if (iter == 0) {
                // last round
                if (number == 0L) {
                    acc
                } else if (numberStr.length % 2 == 0) {
                    acc + 1
                } else {
                    acc
                }
            } else {
                if (number == 0L) {
                    acc + count(iter.dec(), 1L, 0)
                } else if (numberStr.length % 2 == 0) {
                    val str = numberStr
                    acc + count(iter.dec(), str.substring(0, str.length / 2).toLong(), 0) +
                            count(iter.dec(), str.substring(str.length / 2, str.length).toLong(), 0) + 1
                } else {
                    acc + count(iter.dec(), number * 2024, 0)
                }
            }.also {
                cache.put(key, it)
            }
        }
    }

    fun part2(input: List<String>, maxIter: Int): Long {
        val stones = input.first().split(' ').map(String::toInt)

        return stones.sumOf { count(maxIter, it.toLong(), 1) }
    }
}
