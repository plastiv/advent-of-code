package aoc2023

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test
import kotlin.time.measureTime

class Day3 {
    @Test
    fun part1Example() {
        val input =
            $$"""
            467..114..
            ...*......
            ..35..633.
            ......#...
            617*......
            .....+.58.
            ..592.....
            ......755.
            ...$.*....
            .664.598..
            """.trimIndent()
                .lineSequence()
        val result = part1(input)
        assertThat(result).isEqualTo(4361)
    }

    @Test
    fun part1Input() {
        fileInput("Day3.txt").useLines { lineSequence ->
            val duration = measureTime {
                val result = part1(lineSequence)
                assertThat(result).isEqualTo(540131)
            }
            println(duration)
        }
    }

    private fun part1(input: Sequence<String>): Int {
        val lines = input.toList()
        var sum = 0
        val currentNumber = StringBuilder()
        var adjacent = false

        lines.forEachIndexed { x, line ->
            line.forEachIndexed { y, c ->
                if (c.isDigit()) {
                    currentNumber.append(c)
                    if (!adjacent) {

                        // check adjacency
                        // if any of adjacent symbols are not number or dot
                        // 1 2 3
                        // 4 c 5
                        // 6 7 8

                        if (lines.pos1IsSymbol(x, y)) {
                            adjacent = true
                            return@forEachIndexed
                        }
                        if (lines.pos2IsSymbol(x, y)) {
                            adjacent = true
                            return@forEachIndexed
                        }
                        if (lines.pos3IsSymbol(x, y)) {
                            adjacent = true
                            return@forEachIndexed
                        }
                        if (lines.pos4IsSymbol(x, y)) {
                            adjacent = true
                            return@forEachIndexed
                        }
                        if (lines.pos5IsSymbol(x, y)) {
                            adjacent = true
                            return@forEachIndexed
                        }
                        if (lines.pos6IsSymbol(x, y)) {
                            adjacent = true
                            return@forEachIndexed
                        }
                        if (lines.pos7IsSymbol(x, y)) {
                            adjacent = true
                            return@forEachIndexed
                        }
                        if (lines.pos8IsSymbol(x, y)) {
                            adjacent = true
                            return@forEachIndexed
                        }
                    }
                } else if (currentNumber.isNotEmpty() && adjacent) {
                    sum += currentNumber.toString().toInt()
                    // reset
                    currentNumber.clear()
                    adjacent = false
                } else {
                    // skip this number and ignore character
                    currentNumber.clear()
                    adjacent = false
                }
            }
            if (currentNumber.isNotEmpty() && adjacent) {
                sum += currentNumber.toString().toInt()
                currentNumber.clear()
                adjacent = false
            }

        }
        return sum
    }

    private fun Char.isSymbol() = when {
        this.code == '.'.code -> false
        this.isDigit() -> false
        else -> true
    }

    // 1 2 3
    // 4 c 5
    // 6 7 8
    //
    // row--; col--
    private fun List<String>.pos1IsSymbol(row: Int, col: Int): Boolean {
        if (row > 0 && col > 0) { // skip first row and first column
            return this[row.dec()][col.dec()].isSymbol()
        }
        return false
    }

    // row--; col
    private fun List<String>.pos2IsSymbol(row: Int, col: Int): Boolean {
        if (row > 0) { // skip first row
            return this[row.dec()][col].isSymbol()
        }
        return false
    }

    // row--; col++
    private fun List<String>.pos3IsSymbol(row: Int, col: Int): Boolean {
        if (row > 0) { // skip first row
            val line = this[row.dec()]
            if (col < line.length - 1) { // skip last column
                return line[col.inc()].isSymbol()
            }
        }
        return false
    }

    // row; col--
    private fun List<String>.pos4IsSymbol(row: Int, col: Int): Boolean {
        if (col > 0) { // skip first col
           return this[row][col.dec()].isSymbol()
        }
        return false
    }

    // row; col++
    private fun List<String>.pos5IsSymbol(row: Int, col: Int): Boolean {
        val line = this[row]
        if (col < line.length - 1) { //  skip last col
            return line[col.inc()].isSymbol()
        }
        return false
    }

    // row++; col--
    private fun List<String>.pos6IsSymbol(row: Int, col: Int): Boolean {
        if (row < this.size - 1 && col > 0) {
            return this[row.inc()][col.dec()].isSymbol()
        }
        return false
    }

    // row++; col
    private fun List<String>.pos7IsSymbol(row: Int, col: Int): Boolean {
        if (row < this.size - 1) {
            return this[row.inc()][col].isSymbol()
        }
        return false
    }

    // row++; col++
    private fun List<String>.pos8IsSymbol(row: Int, col: Int): Boolean {
        if (row < this.size - 1) {
            val line = this[row.inc()]
            if (col < line.length - 1) {
                return line[col.inc()].isSymbol()
            }
        }
        return false
    }
}
