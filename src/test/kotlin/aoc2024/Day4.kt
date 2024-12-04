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
            assertThat(result).isEqualTo(2567)
        }
        println("part1 $duration")
    }

    fun String.countXmas(): Int {
        return windowed(4).count { string -> string.contains("XMAS") || string.contains("SAMX") }
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
    fun part2Example() {
        val input =
            $"""
            .M.S......
            ..A..MSMS.
            .M.S.MAA..
            ..A.ASMSM.
            .M.S.M....
            ..........
            S.S.S.S.S.
            .A.A.A.A..
            M.M.M.M.M.
            ..........
            """.trimIndent()
                .lines()
        val result = part2(input)
        assertThat(result).isEqualTo(9)
    }

    @Test
    fun part2Input() {
        val lines = fileInput("Day4.txt").readLines()
        val duration = measureTime {
            val result = part2(lines)
            assertThat(result).isEqualTo(2029)
        }
        println("part2 $duration")
    }

    fun part2(input: List<String>): Int {
        val elements = input.mapIndexed { row, str ->
            str.mapIndexed { col, char ->
                Node(Position(col, row), char)
            }
        }.flatten()
            .associateBy { it.position }

        println("Count lines")
        return elements.filter { entry ->
            entry.value.value == 'A'
        }.count { entry ->
            elements.nearbyWindow2(entry.value.position).also(::println) == 2
        }
    }


    fun Map<Position, Node>.nearbyWindow2(position: Position): Int {

        return listOf(
            listOf(position.southEast(), position, position.northWest()),
            listOf(position.northEast(), position, position.southWest()),
        ).map { positions ->
            positions.mapNotNull { position ->
                this[position]?.value
            }
        }.filter { it.size == 3 }
            .map { chars -> chars.joinToString("") }
            .count { str -> str == "MAS" || str == "SAM" }

    }
}
