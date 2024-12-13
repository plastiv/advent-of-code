package aoc2023

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

class Day1 {
    @Test
    fun part1ExampleValues() {
        val calibrationInput =
            """
            1abc2
            pqr3stu8vwx
            a1b2c3d4e5f
            treb7uchet
            """.trimIndent()
                .lines()
        val result = part1Solution(calibrationInput)
        assertThat(result).isEqualTo(142)
    }

    @Test
    fun part1InputValues() {
        val input = fileInput("Day1.txt").readLines()
        val result = part1Solution(input)
        assertThat(result).isEqualTo(54159)
    }

    @Test
    fun part2ExampleValues() {
        val calibrationInput =
            """
            two1nine
            eightwothree
            abcone2threexyz
            xtwone3four
            4nineeightseven2
            zoneight234
            7pqrstsixteen    
            """.trimIndent()
                .lines()
        val result = part2Solution(calibrationInput)
        assertThat(result).isEqualTo(281)
    }

    @Test
    fun part2InputValues() {
        val input = fileInput("Day1.txt").readLines()
        val result = part2Solution(input)
        assertThat(result).isEqualTo(53866)
    }

    fun part1Solution(input: List<String>): Number =
        input
            .sumOf { line ->
                val firstDigitChar = line.first { it.isDigit() }
                val lastDigitChar = line.last { it.isDigit() }
                "$firstDigitChar$lastDigitChar".toInt()
            }

    val validDigits =
        listOf<String>(
            "1",
            "2",
            "3",
            "4",
            "5",
            "6",
            "7",
            "8",
            "9",
            "one",
            "two",
            "three",
            "four",
            "five",
            "six",
            "seven",
            "eight",
            "nine",
        )

    fun part2Solution(input: List<String>): Number =
        input
            .sumOf { line ->
                val firstDigitChar =
                    mapToDigit(line.findAnyOf(validDigits)!!.second)
                val lastDigitChar = mapToDigit(line.findLastAnyOf(validDigits)!!.second)
                "$firstDigitChar$lastDigitChar".toInt()
            }

    private fun mapToDigit(find: String): String =
        when (find) {
            "1" -> "1"
            "2" -> "2"
            "3" -> "3"
            "4" -> "4"
            "5" -> "5"
            "6" -> "6"
            "7" -> "7"
            "8" -> "8"
            "9" -> "9"
            "one" -> "1"
            "two" -> "2"
            "three" -> "3"
            "four" -> "4"
            "five" -> "5"
            "six" -> "6"
            "seven" -> "7"
            "eight" -> "8"
            "nine" -> "9"
            else -> error("Shouldn't happen, but was $find")
        }
}
