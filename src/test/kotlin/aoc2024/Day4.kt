package aoc2024

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test
import kotlin.time.measureTime

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
        return windowed(4).count { string -> string.contains("XMAS") || string.contains("SAMX") }
    }

    @Test
    fun testCount() {
        assertThat("XMAS".countXmas()).isEqualTo(1)
        assertThat("SAMX".countXmas()).isEqualTo(1)
        assertThat("MMMSXXMASM".countXmas()).isEqualTo(1)
        assertThat("XMAS".countXmas()).isEqualTo(1)
    }

    fun part1(input: List<String>): Int {

        input.map { line ->
            line.map { ch ->
                when (ch) {
                    'X', 'M', 'A', 'S' -> ch
                    else -> '.'
                }
            }
        }.forEach(::println)

        println("Count lines")
        var count = input.sumOf { it.countXmas() }

        val chars = input.map { string -> string.toList() }

        println("transpose")

        println("Count columns")
        count += chars.transpose().map { chars -> chars.joinToString("") }
            .sumOf { it.countXmas() }

        println("diagonals")
        count += chars.diagonals().sumOf { it.countXmas()       }

        count += chars.transpose().diagonals().sumOf { it.countXmas() }
        return count
    }

    fun <T> List<List<T>>.transpose(): List<List<T>> {
        return (this[0].indices).map { i -> (this.indices).map { j -> this[j][i] } }
    }


    fun List<List<Char>>.diagonals(): List<String> {
        val mList = mutableListOf<String>()
        // main
        val main =
            (this.indices).map { j -> this[j][j] }.joinToString("")
        mList.add(main)
        // diagonal up (right)
        for (diagN in 1..this[0].size - 1) {
            println("Look diagonal $diagN")
            val diag = mutableListOf<Char>()
            var col = diagN
            for (row in 0..(this.size - diagN - 1)) {
//                println("row $row col $col")
                diag.add(this[row][col++])
            }

            mList.add(diag.joinToString(""))
            println(mList.last())
        }
        // diagonal down left
        for (diagN in 1..this.size - 1) {
            println("Look diagonal $diagN")
            val diag = mutableListOf<Char>()
            var row = diagN
            for (col in 0..(this.size - diagN - 1)) {
//                println("row $row col $col")
                diag.add(this[row++][col])
            }

            mList.add(diag.joinToString(""))
            println(mList.last())
        }
        return mList.toList()
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
