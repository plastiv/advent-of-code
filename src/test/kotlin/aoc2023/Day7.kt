@file:OptIn(ExperimentalStdlibApi::class)

package aoc2023

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test
import kotlin.time.measureTime

class Day7 {
    @Test
    fun part1Example() {
        val input =
            $"""
            32T3K 765
            T55J5 684
            KK677 28
            KTJJT 220
            QQQJA 483
            """.trimIndent()
                .lines()
        val result = part1(input)
        assertThat(result).isEqualTo(6440)
    }

    @Test
    fun part1Input() {
        val lines = fileInput("Day7.txt").readLines()
        val duration = measureTime {
            val result = part1(lines)
            assertThat(result).isEqualTo(249390788)
        }
        println("part1 $duration")
    }

    data class Hand(val string: String) {

        val value: Int

        init {

            // score highest card A, K, Q, J, T, 9, 8, 7, 6, 5, 4, 3, or 2\
            val cardsScore = string.map { ch ->
                return@map when (ch) {
                    'A' -> "F"
                    'K' -> "E"
                    'Q' -> "D"
                    'J' -> "C"
                    'T' -> "B"
                    '9' -> "A"
                    '8' -> "9"
                    '7' -> "8"
                    '6' -> "7"
                    '5' -> "6"
                    '4' -> "5"
                    '3' -> "4"
                    '2' -> "3"
                    else -> error("Shouldn't happen, but was $ch")
                }
            }.joinToString("")

            // score highest hand
            val distinctCards = string.toList()
                .groupingBy { it }
                .eachCount()
                .values
                .sorted()
                .joinToString("")

            val handScore = when (distinctCards) {
                "5" -> "F"
                "14" -> "E"
                "23" -> "D"
                "113" -> "C"
                "122" -> "B"
                "1112" -> "A"
                "11111" -> "9"
                else -> error("Shouldn't happen but was $distinctCards")
            }

            value = (handScore + cardsScore).hexToInt()
        }
    }


    fun part1(input: List<String>): Int {
        return input
            .map { str ->
                str.split(' ')
                    .zipWithNext()
            }
            .flatten()
            .map { (hand, bid) ->
                Pair(Hand(hand), bid.toInt())
            }
            .sortedBy { (hand, _) ->
                hand.value
            }.apply {
                this.forEach { (hand, bid) ->
                    println("H(${hand.string}, ${hand.value.toHexString()}, ${hand.value}) $bid")
                }
            }
            .foldIndexed(0) { rank, acc, (_, bid) -> acc + bid * (rank + 1) }
            .apply(::println)
    }
}
