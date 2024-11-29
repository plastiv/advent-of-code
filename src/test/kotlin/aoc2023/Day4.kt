package aoc2023

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test
import kotlin.time.measureTime

class Day4 {
    @Test
    fun part1Example() {
        val input =
            $$"""
            Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
            Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
            Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
            Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
            Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
            Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11
            """.trimIndent()
                .lineSequence()
        val result = part1(input)
        assertThat(result).isEqualTo(13)
    }

    @Test
    fun part1Input() {
        fileInput("Day4.txt").useLines { lineSequence ->
            val duration = measureTime {
                val result = part1(lineSequence)
                assertThat(result).isEqualTo(24542)
            }
            println("part1 $duration")
        }
    }

    private fun part1(input: Sequence<String>): Int {
        return input.sumOf { card ->
            val numbers = card.substringAfter(": ")
                .split(" | ")

            val winningNumbers = numbers[0]
                .split(' ')
                .filterNot { it.isEmpty() }
                .map(String::toInt)

            val cardNumbers = numbers[1]
                .split(' ')
                .filterNot { it.isEmpty() }
                .map(String::toInt)

            val count = cardNumbers.count { i ->
                winningNumbers.contains(i)
            }

            return@sumOf when {
                count == 0 -> 0
                count == 1 -> 1
                count > 1 -> {
                    var points = 1
                    repeat(count - 1) { _ -> points = points * 2 }
                    points
                }

                else -> error("Can't happen, but was $count")
            }
        }
    }

    @Test
    fun part2Example() {
        val input =
            $$"""
            Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
            Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
            Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
            Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
            Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
            Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11
            """.trimIndent()
                .lineSequence()
        val result = part2(input)
        assertThat(result).isEqualTo(30)
    }

    @Test
    fun part2Input() {
        fileInput("Day4.txt").useLines { lineSequence ->
            val duration = measureTime {
                val result = part2(lineSequence)
                assertThat(result).isEqualTo(8736438)
            }
            println("part2 $duration")
        }
    }

    private fun part2(input: Sequence<String>): Int {
        val stack = ArrayDeque<Int>()
        var total = 0

        // card to matching numbers count
        val matchingNumber = mutableMapOf<Int, Int>()
        input.forEachIndexed { index, card ->
            total++

            val (_, winningNumbersPart, cardNumbersPart) = card
                .split(":", " | ")

            val winningNumbers = winningNumbersPart
                .split(' ')
                .filterNot { it.isEmpty() }
                .map(String::toInt)

            val cardNumbers = cardNumbersPart
                .split(' ')
                .filterNot { it.isEmpty() }
                .map(String::toInt)

            val count = winningNumbers.count { i ->
                cardNumbers.contains(i)
            }
            matchingNumber.put(index, count)

            for (i in index + 1..index + count) {
                // next count cards are get +1
                stack.addLast(i)
            }
        }
        while (stack.isNotEmpty()) {

            total++

            val index = stack.removeFirst()
            val count = matchingNumber[index]!!
            for (i in index + 1..index + count) {
                // next count cards are get +1
                stack.addLast(i)
            }
        }

        return total
    }
}
