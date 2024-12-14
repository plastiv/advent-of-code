package aoc2024

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test
import kotlin.math.abs
import kotlin.time.measureTime

class Day14 {
    @Test
    fun part1Example() {
        val input =
            """
            p=0,4 v=3,-3
            p=6,3 v=-1,-3
            p=10,3 v=-1,2
            p=2,0 v=2,-1
            p=0,0 v=1,3
            p=3,0 v=-2,-2
            p=7,6 v=-1,-3
            p=3,0 v=-1,-2
            p=9,3 v=2,3
            p=7,3 v=-1,2
            p=2,4 v=2,-3
            p=9,5 v=-3,-3
            """.trimIndent()
                .lines()
        val result = part1(input, 7, 11)
        assertThat(result).isEqualTo(12)
    }

    @Test
    fun part1Input() {
        val lines = fileInput("Day14.txt").readLines()
        val duration =
            measureTime {
                val result = part1(lines, 103, 101)
                assertThat(result).isEqualTo(226236192)
            }
        println("part1 $duration")
    }

    fun part1(
        input: List<String>,
        rowSize: Int,
        colSize: Int,
    ): Long = solution(input, rowSize, colSize)

    private fun solution(
        input: List<String>,
        rowSize: Int,
        colSize: Int,
    ): Long =
        input
            .map { str ->
                val (pcol, prow) =
                    str
                        .substringAfter("p=")
                        .substringBefore(" v=")
                        .split(',')
                        .map(String::toInt)
                val (vcol, vrow) =
                    str
                        .substringAfter(" v=")
                        .split(',')
                        .map(String::toInt)
                Pair(prow to pcol, vrow to vcol)
            }.also(::println)
            .map { (position, velocity) ->
                val (prow, pcol) = position
                val (vrow, vcol) = velocity
                // t=0 4 (4 - 3 * 0) 4 - 0
                // t=1 1 (4 - 3 * 1) 4 - 3
                // t=2 5 (4 - 3 * 2) 4 - 6
                // t=3 2 (4 - 3 * 3) 4 - 9
                // t=4   (4 - 3 * 4) 4 - 12
                //                for (i in 0..5) {
                //                    val row = (prow + vrow * i) % 7
                //                    val col = (pcol + vcol * i) % 11
                //
                //                    val prettyRow = if (row < 0) 7 - abs(row) else row
                //                    val prettyCol = if (col < 0) 11 - abs(col) else col
                //                    println("h $i r=$prettyRow c=$prettyCol")
                //                }
                val drow = prow + vrow * 100
                val dcol = pcol + vcol * 100
                drow to dcol
            }.map { (row, col) ->
                row % rowSize to col % colSize
            }.map { (row, col) ->
                val prettyRow = if (row < 0) rowSize - abs(row) else row
                val prettyCol = if (col < 0) colSize - abs(col) else col

                prettyRow to prettyCol
            }.also(::println)
            .filterNot { (row, col) ->
                row == (rowSize - 1) / 2 || col == (colSize - 1) / 2
            }.also(::println)
            .groupBy { (row, col) ->
                val part1 =
                    if (row < (rowSize - 1) / 2) {
                        0
                    } else {
                        1
                    }
                val part2 =
                    if (col < (colSize - 1) / 2) {
                        0
                    } else {
                        1
                    }
                "$part1$part2"
            }.entries
            .fold(1) { acc, entry ->
                acc * entry.value.size
            }

    @Test
    fun part2Example1() {
        val input =
            """
            """.trimIndent()
                .lines()
        val result = part2(input)
        assertThat(result).isEqualTo(0)
    }

    @Test
    fun part2Input() {
        val lines = fileInput("Day14.txt").readLines()
        val duration =
            measureTime {
                val result = part2(lines)
                assertThat(result).isEqualTo(0)
            }
        println("part2 $duration")
    }

    fun part2(input: List<String>): Long = 0
}
