package aoc2023

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

class Day1Test {
    @Test
    fun sumAllValues() {
        val calibrationInput =
            """
            1abc2
            pqr3stu8vwx
            a1b2c3d4e5f
            treb7uchet
            """.trimIndent()
        val result = sumAllValues(calibrationInput)
        assertThat(result).isEqualTo(142)
    }
}
