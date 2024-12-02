package aoc2024

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.math.abs
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
            .filter { ints ->
                ints
                    .windowed(2)
                    .all { (first, second): List<Int> ->
                        when (abs(second - first)) {
                            1, 2, 3 -> true
                            else -> false
                        }
                    }
            }.filter { ints ->
                val allIncreasing = ints
                    .windowed(2)
                    .all { (first, second) ->
                        first <= second
                    }
                val allDecreasing = ints
                    .windowed(2)
                    .all { (first, second) ->
                        first >= second
                    }
                allIncreasing || allDecreasing
            }
            .also(::println)
            .count()
    }

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
        println("part1 $duration")
    }

    fun List<Int>.allInc(): Boolean = this.windowed(2)
        .all { (first, second) ->
            when (second - first) {
                1, 2, 3 -> true
                else -> false
            }
        }

    

    fun part2(input: List<String>): Int {
        return input.map { str ->
            str.split(' ').filterNot(String::isEmpty).map(String::toInt)
        }
            .filter { ints: List<Int> ->
                val check = chekc(ints)
                println("$check ${ints.toString()}")
                return@filter check
            }
            .also(::println)
            .count()
    }

    private fun chekc(ints: List<Int>): Boolean {
        if (ints.allInc()) {
            return true
        }

        if (removeSecondNonInc(ints).allInc()) {
            return true
        }

        if (removeFirstNonInc(ints).allInc()) {
            return true
        }

        if (ints.reversed().allInc()) {
            return true
        }

        if (removeSecondNonInc(ints.reversed()).allInc()) {
            return true
        }

        if (removeFirstNonInc(ints.reversed()).allInc()) {
            return true
        }
        return false
    }

    private fun removeSecondNonInc(ints: List<Int>): MutableList<Int> {
        val windowed = ints.withIndex()
            .windowed(2)

        val find = windowed.find { (first, second) ->
            when (second.value - first.value) {
                1, 2, 3 -> false
                else -> true
            }
        }
//        ints.toMutableList().removeAll { i ->  }

        val (_, second) = find!!
        val toMutableList = ints.toMutableList()
        toMutableList.removeAt(second.index)
        return toMutableList
    }

    private fun removeFirstNonInc(ints: List<Int>): MutableList<Int> {
        val windowed = ints.withIndex()
            .windowed(2)

        val find = windowed.find { (first, second) ->
            when (second.value - first.value) {
                1, 2, 3 -> false
                else -> true
            }
        }
//        ints.toMutableList().removeAll { i ->  }

        val (first, _) = find!!
        val toMutableList = ints.toMutableList()
        toMutableList.removeAt(first.index)
        return toMutableList
    }
}
