package aoc2024

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test
import kotlin.time.measureTime

val <X, Y> Pair<X, Y>.x
    get() = this.first

val <X, Y> Pair<X, Y>.y
    get() = this.second

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
                assertThat(result).isEqualTo(36571)
            }
        println("part1 $duration")
    }

    fun part1(input: List<String>): Long = solution(input, 0)

    @Test
    fun part2Example1() {
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
        val result = part2(input)
        assertThat(result).isEqualTo(875318608908)
    }

    @Test
    fun part2Input() {
        val lines = fileInput("Day13.txt").readLines()
        val duration =
            measureTime {
                val result = part2(lines)
                assertThat(result).isEqualTo(85527711500010)
            }
        println("part2 $duration")
    }

    fun part2(input: List<String>): Long = solution(input, 10000000000000)

    private fun solution(
        input: List<String>,
        prizeBonus: Long,
    ) = input
        .chunked(4)
        .map { (lineA, lineB, lineP) ->
            val ax = lineA.substringAfter("Button A: X+").substringBefore(", Y+").toInt()
            val ay = lineA.substringAfter("Y+").toInt()
            val bx = lineB.substringAfter("Button B: X+").substringBefore(", Y+").toInt()
            val by = lineB.substringAfter("Y+").toInt()
            val px = lineP.substringAfter("Prize: X=").substringBefore(", Y=").toInt()
            val py = lineP.substringAfter(", Y=").toInt()
            Triple(ax to ay, bx to by, prizeBonus + px to prizeBonus + py)
        }.mapNotNull { (a, b, p) ->
            // algebraic equation
            // find A and B, rest are constants
            // A * ax + B * bx = px
            // A * ay + B * by = py
            val timesA =
                (a.x * p.y - a.y * p.x) /
                    (b.y * a.x - b.x * a.y.toDouble())
            val timesB =
                (b.x * p.y - b.y * p.x) /
                    (b.x * a.y - a.x * b.y.toDouble())
            if (timesB.rem(1) == 0.0 && timesA.rem(1) == 0.0) { // is division result a whole number?
                return@mapNotNull Pair(timesB.toLong(), timesA.toLong())
            } else {
                return@mapNotNull null
            }
        }.sumOf { (a, b) ->
            a * 3 + b
        }
}
