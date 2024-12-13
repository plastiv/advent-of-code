package aoc2024

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test
import kotlin.time.measureTime

typealias Button = Pair<Int, Int>
typealias ButtonL = Pair<Long, Long>

fun Button.x() = this.first

fun ButtonL.x() = this.first

fun Button.y() = this.second

fun ButtonL.y() = this.second

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

    fun part1(input: List<String>): Int {
        return input
            .chunked(4)
            .map { (buttonA, buttonB, prize, _) ->
                val buttonAx = buttonA.substringAfter("Button A: X+").substringBefore(", Y+").toInt()
                val buttonAy = buttonA.substringAfter("Y+").toInt()
                val buttonA = buttonAx to buttonAy
                val buttonBx = buttonB.substringAfter("Button B: X+").substringBefore(", Y+").toInt()
                val buttonBy = buttonB.substringAfter("Y+").toInt()
                val buttonB = buttonBx to buttonBy
                val prizeX = prize.substringAfter("Prize: X=").substringBefore(", Y=").toInt()
                val prizeY = prize.substringAfter(", Y=").toInt()
                val prize = prizeX to prizeY
                Triple(buttonA, buttonB, prize)
            }.also(::println)
            .mapNotNull { (buttonA: Button, buttonB: Button, prize: Button) ->
                for (a in 0..100) {
                    val tmpAX = buttonA.x() * a
                    val tmpAY = buttonA.y() * a
                    for (b in 0..100) {
                        val tmpBX = buttonB.x() * b
                        val tmpBY = buttonB.y() * b
                        if (tmpAX + tmpBX == prize.x() && tmpAY + tmpBY == prize.y()) {
                            println("found w: $a $b")
                            return@mapNotNull a to b
                        }
                    }
                }
                return@mapNotNull null
            }.sumOf { (a, b) ->
                a * 3 + b
            }
    }

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
        assertThat(result).isEqualTo(480)
    }

    @Test
    fun part2Input() {
        val lines = fileInput("Day13.txt").readLines()
        val duration =
            measureTime {
                val result = part2(lines)
                assertThat(result).isEqualTo(36571)
            }
        println("part2 $duration")
    }

    fun part2(input: List<String>): Long {
        val result = (94 * 5400 - 34 * 8400) / (67 * 94 - 22 * 34)
        val result2 = (22 * 5400 - 67 * 8400) / (22 * 34 - 94 * 67)

        println("res $result")
        println("res $result2")
        return input
            .chunked(4)
            .map { (buttonA, buttonB, prize, _) ->
                val buttonAx = buttonA.substringAfter("Button A: X+").substringBefore(", Y+").toLong()
                val buttonAy = buttonA.substringAfter("Y+").toLong()
                val buttonA = buttonAx to buttonAy
                val buttonBx = buttonB.substringAfter("Button B: X+").substringBefore(", Y+").toLong()
                val buttonBy = buttonB.substringAfter("Y+").toLong()
                val buttonB = buttonBx to buttonBy
                val prizeX = prize.substringAfter("Prize: X=").substringBefore(", Y=").toLong()
                val prizeY = prize.substringAfter(", Y=").toLong()
                // 10000000000000
                // 10000000008400
                val prize = 10000000000000 + prizeX to 10000000000000 + prizeY
//                val prize = prizeX to prizeY
                Triple(buttonA, buttonB, prize)
            }.also(::println)
            .mapNotNull { (buttonA: ButtonL, buttonB: ButtonL, prize: ButtonL) ->
                val b =
                    (buttonA.x() * prize.y() - buttonA.y() * prize.x()) /
                        (buttonB.y() * buttonA.x() - buttonB.x() * buttonA.y().toDouble())
                val a =
                    (buttonB.x() * prize.y() - buttonB.y() * prize.x()) /
                        (buttonB.x() * buttonA.y() - buttonA.x() * buttonB.y().toDouble())
                if (a.rem(1) == 0.0 && b.rem(1) == 0.0) {
                    return@mapNotNull Pair(a.toLong(), b.toLong())
                } else {
                    return@mapNotNull null
                }
            }.sumOf { (a, b) ->
                a * 3 + b
            }
    }
}
