package aoc2024

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test
import kotlin.time.measureTime

typealias Button = Pair<Int, Int>

fun Button.x() = this.first

fun Button.y() = this.second

class Day13 {
    @Test
    fun part1Example() {
        val input =
            """
            Button A: X+94, Y+34
            Button B: X+22, Y+67
            Prize: X=8400, Y=5400

            Button A: X+26, Y+66
            Button B: X+67, Y+21
            Prize: X=12748, Y=12176

            Button A: X+17, Y+86
            Button B: X+84, Y+37
            Prize: X=7870, Y=6450

            Button A: X+69, Y+23
            Button B: X+27, Y+71
            Prize: X=18641, Y=10279
            """.trimIndent()
                .lines()
        val result = part1(input)
        assertThat(result).isEqualTo(480)
    }

    @Test
    fun part1Input() {
        val lines = fileInput("Day13.txt").readLines()
        val duration =
            measureTime {
                val result = part1(lines)
                assertThat(result).isEqualTo(0)
            }
        println("part1 $duration")
    }

}
