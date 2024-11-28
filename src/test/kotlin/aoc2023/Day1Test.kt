package aoc2023

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

class Day1Test {
    @Test
    fun sumExampleValues() {
        val calibrationInput =
            """
            1abc2
            pqr3stu8vwx
            a1b2c3d4e5f
            treb7uchet
            """.trimIndent()
                .lines()
        val result = sumAllValues(calibrationInput)
        assertThat(result).isEqualTo(142)
    }

    @Test
    fun sumInputValues() {
        val input = fileInput("Day1Input.txt").readLines()
        val result = sumAllValues(input)
        assertThat(result).isEqualTo(54159)
    }
}
