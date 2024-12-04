package aoc2024

import aoc2024.Day4.Position
import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test
import kotlin.time.measureTime

fun Position.north() = copy(col, row.dec())
fun Position.northEast() = copy(col.dec(), row.dec())
fun Position.northWest() = copy(col.inc(), row.dec())
fun Position.east() = copy(col.dec(), row)
fun Position.west() = copy(col.inc(), row)
fun Position.south() = copy(col, row.inc())
fun Position.southEast() = copy(col.dec(), row.inc())
fun Position.southWest() = copy(col.inc(), row.inc())

class Day4 {
    @Test
    fun part1Example() {
        val input =
            $"""
            MMMSXXMASM
            MSAMXMSMSA
            AMXSXMAAMM
            MSAMASMSMX
            XMASAMXAMM
            XXAMMXXAMA
            SMSMSASXSS
            SAXAMASAAA
            MAMMMXMMMM
            MXMXAXMASX
            """.trimIndent()
                .lines()
        val result = part1(input)
        assertThat(result).isEqualTo(18)
    }

    @Test
    fun part1Input() {
        val lines = fileInput("Day4.txt").readLines()
        val duration = measureTime {
            val result = part1(lines)
            assertThat(result).isEqualTo(2552)
        }
        println("part1 $duration")
    }

    fun String.countXmas(): Int {
        return windowed(4).count { string -> string.contains("XMAS")  }
    }

    @Test
    fun testCount() {
        assertThat("XMAS".countXmas()).isEqualTo(1)
        assertThat("SAMX".countXmas()).isEqualTo(1)
        assertThat("MMMSXXMASM".countXmas()).isEqualTo(1)
        assertThat("XMAS".countXmas()).isEqualTo(1)
    }

    data class Position(val col: Int, val row: Int)
    data class Node(val position: Position, val value: Char)

    fun Map<Position, Node>.myWindow(initPosition: Position, operation: (Position) -> Position): String {
        return (0..2).fold(initPosition to "${this[initPosition]!!.value}") { (position, acc), _ ->
            val newPosition = operation(position)
            val newChar = this[newPosition]?.value
            val newAcc = if (newChar == null) acc else acc + newChar
            newPosition to newAcc
        }.second
    }

    fun Map<Position, Node>.nearbyWindow(position: Position): List<String> {

        return listOf(
            myWindow(position, Position::north),
            myWindow(position, Position::northEast),
            myWindow(position, Position::northWest),
            myWindow(position, Position::east),
            myWindow(position, Position::west),
            myWindow(position, Position::south),
            myWindow(position, Position::southEast),
            myWindow(position, Position::southWest),
        ).filter { it.length == 4 }
    }

    fun part1(input: List<String>): Int {

        val elements = input.mapIndexed { row, str ->
            str.mapIndexed { col, char ->
                Node(Position(col, row), char)
            }
        }.flatten()
            .associateBy { it.position }

        println("Count lines")
        return elements.filter { entry ->
            entry.value.value == 'X'
        }.map { entry ->
            elements.nearbyWindow(entry.value.position).also(::println).count { it == "XMAS" }
        }.sum()

    }

    @Test
    fun part2Input() {
        val lines = fileInput("Day3.txt").readLines()
        val duration = measureTime {
            val result = part2(lines)
            assertThat(result).isEqualTo(88802350)
        }
        println("part2 $duration")
    }

    fun part2(input: List<String>): Int {
        val regex = """mul\(\d+,\d+\)|don't\(\)|do\(\)""".toRegex()
        var doCount = true
        return input.sumOf { str ->
            regex.findAll(str).sumOf { result ->
                val match = result.groupValues[0]
                when (match) {
                    "do()" -> {
                        doCount = true
                        0
                    }

                    "don't()" -> {
                        doCount = false
                        0
                    }

                    else -> if (doCount) {
                        val first = match.substringAfter("mul(").substringBefore(",")
                        val second = match.substringAfter(",").substringBefore(")")
                        first.toInt() * second.toInt()
                    } else {
                        0
                    }
                }

            }
        }
    }
}
