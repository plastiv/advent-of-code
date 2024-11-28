package aoc2023

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

class Day2Test {
    @Test
    fun part1ExampleValues() {
        val calibrationInput =
            """
            Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
            Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
            Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
            Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
            Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
            """.trimIndent()
                .lineSequence()
        val result = gameOfCubes1(calibrationInput)
        assertThat(result).isEqualTo(8)
    }

    @Test
    fun part1InputValues() {
        val result =
            fileInput("Day2Input.txt").useLines { lineSequence ->
                gameOfCubes1(lineSequence)
            }

        assertThat(result).isEqualTo(2505)
    }

    @Test
    fun part2ExampleValues() {
        val calibrationInput =
            """
            Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
            Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
            Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
            Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
            Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
            """.trimIndent()
                .lineSequence()
        val result = gameOfCubes2(calibrationInput)
        assertThat(result).isEqualTo(2286)
    }

    @Test
    fun part2InputValues() {
        val result =
            fileInput("Day2Input.txt")
                .useLines { lineSequence ->
                    gameOfCubes2(lineSequence)
                }

        assertThat(result).isEqualTo(70265)
    }
}
