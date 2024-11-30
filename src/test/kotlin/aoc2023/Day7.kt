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

    @Test
    fun part2Example() {
        val input =
            $"""
            32T3K 765
            T55J5 684
            KK677 28
            KTJJT 220
            QQQJA 483
            """.trimIndent()
                .lines()
        val result = part2(input)
        assertThat(result).isEqualTo(5905)
    }

    @Test
    fun part2Input() {
        val lines = fileInput("Day7.txt").readLines()
        val duration = measureTime {
            val result = part2(lines)
            assertThat(result).isEqualTo(248750248)
        }
        println("part1 $duration")
    }

    data class Hand2(val string: String) {

        val value: Int

        init {

            // score highest card A, K, Q, J, T, 9, 8, 7, 6, 5, 4, 3, or 2\
            val cardsScore = string.map { ch ->
                return@map when (ch) {
                    'A' -> "F"
                    'K' -> "E"
                    'Q' -> "D"
                    'T' -> "C"
                    '9' -> "B"
                    '8' -> "A"
                    '7' -> "9"
                    '6' -> "8"
                    '5' -> "7"
                    '4' -> "6"
                    '3' -> "5"
                    '2' -> "4"
                    'J' -> "3"
                    else -> error("Shouldn't happen, but was $ch")
                }
            }.joinToString("")

            val jCount = string.count { it == 'J'  }
            // score highest hand
            val distinctCards = string.filterNot { it == 'J' }.toList()
                .groupingBy { it }
                .eachCount()
                .toMutableMap()
                .also(::println)
                .values
                .sorted()
                .toMutableList()

            if (distinctCards.isNotEmpty()) {
                distinctCards[distinctCards.size - 1] += jCount
            } else {
                distinctCards.add(jCount) // all 5 are J
            }
            val handScore = when (distinctCards.joinToString("")) {
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

    fun part2(input: List<String>): Int {
        return input
            .map { str ->
                str.split(' ')
                    .zipWithNext()
            }
            .flatten()
            .map { (hand, bid) ->
                Pair(Hand2(hand), bid.toInt())
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
