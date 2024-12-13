package aoc2023

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

class Day2 {
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
            fileInput("Day2.txt").useLines { lineSequence ->
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
            fileInput("Day2.txt")
                .useLines { lineSequence ->
                    gameOfCubes2(lineSequence)
                }

        assertThat(result).isEqualTo(70265)
    }

    fun gameOfCubes1(input: Sequence<String>): Int =
        input
            .sumOf { line ->
                // skip games with over 12 red, 13 green or  14 blue cubes
                val any =
                    line
                        .dropWhile { it != ':' }
                        .splitToSequence(';', ',')
                        .any { ballsCount ->
                            val count = ballsCount.filter { it.isDigit() }.toInt()

                            return@any when {
                                ballsCount.contains("red") -> count > 12
                                ballsCount.contains("green") -> count > 13
                                ballsCount.contains("blue") -> count > 14
                                else -> error("Should not happen, but was $ballsCount")
                            }
                        }

                return@sumOf if (any) {
                    0
                } else {
                    // game #
                    line.removePrefix("Game ").takeWhile { it != ':' }.toInt()
                }
            }

    fun gameOfCubes2(input: Sequence<String>): Int =
        input.sumOf { line ->
            var maxGreen = 0
            var maxRed = 0
            var maxBlue = 0

            line
                .dropWhile { it != ':' }
                .splitToSequence(';', ',')
                .forEach { str ->
                    val count = str.filter { it.isDigit() }.toInt()

                    when {
                        str.contains("red") -> if (count > maxRed) maxRed = count
                        str.contains("green") -> if (count > maxGreen) maxGreen = count
                        str.contains("blue") -> if (count > maxBlue) maxBlue = count
                        else -> error("Should not happen, but was $str")
                    }
                }

            maxGreen * maxRed * maxBlue
        }
}
