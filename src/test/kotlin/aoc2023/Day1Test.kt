package aoc2023

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

class Day1Test {
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
        val input = fileInput("Day1Input.txt").readLines()
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
        val input = fileInput("Day1Input.txt").readLines()
        val result = part2Solution(input)
        assertThat(result).isEqualTo(53866)
    }
}
