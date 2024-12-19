package aoc2024

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test
import kotlin.time.measureTime

class Day19 {
    @Test
    fun part1Example1() {
        val input =
            """
            r, wr, b, g, bwu, rb, gb, br

            brwrr
            bggr
            gbbr
            rrbgbr
            ubwu
            bwurrg
            brgr
            bbrgwb
            """.trimIndent()
                .lines()
        val result = part1(input)
        assertThat(result).isEqualTo(6)
    }

    @Test
    fun part1Input() {
        val lines = fileInput("Day19.txt").readLines()
        val duration =
            measureTime {
                val result = part1(lines)
                assertThat(result).isEqualTo(260)
                // 243 is too low
            }
        println("part1 $duration")
    }

    fun part1(input: List<String>): Int {
        val alphabet =
            input
                .first()
                .split(", ")
                .map(String::reversed)
//                .sortedByDescending { string -> string.length }
        val words =
            input
                .drop(2)
                .map(String::reversed)

        fun f(tail: String): Boolean {
            return if (tail.isEmpty()) {
                true
            } else {
                alphabet
                    .any { prefix ->
                        return@any if (tail.startsWith(prefix)) {
                            f(tail.removePrefix(prefix))
                        } else {
                            false
                        }
                    }
            }
        }
        return words.count { f(it) }
    }

    @Test
    fun part2Example1() {
        val input =
            """
            r, wr, b, g, bwu, rb, gb, br

            brwrr
            bggr
            gbbr
            rrbgbr
            ubwu
            bwurrg
            brgr
            bbrgwb
            """.trimIndent()
                .lines()
        val result = part2(input)
        assertThat(result).isEqualTo(16)
    }

    @Test
    fun part2Input() {
        val lines = fileInput("Day19.txt").readLines()
        val duration =
            measureTime {
                val result = part2(lines)
                assertThat(result).isEqualTo(639963796864990)
            }
        println("part2 $duration")
    }

    fun part2(input: List<String>): Long {
        val alphabet =
            input
                .first()
                .split(", ")
                .map(String::reversed)
//                .sortedByDescending { string -> string.length }
        val words =
            input
                .drop(2)
                .map(String::reversed)

        fun count(
            word: String,
            cache: MutableMap<String, Long>,
        ): Long =
            cache.getOrPut(word) {
                if (word.isEmpty()) {
                    1
                } else {
                    alphabet
                        .filter { word.startsWith(it) }
                        .sumOf { count(word.removePrefix(it), cache) }
                }
            }

        val cache = mutableMapOf<String, Long>()
        return words.sumOf { count(it, cache) }
    }
}
