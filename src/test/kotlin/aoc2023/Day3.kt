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

    @Test
    fun part2Example() {
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
        val result = part2(input)
        assertThat(result).isEqualTo(467835)
    }

    @Test
    fun part2Input() {
        fileInput("Day3.txt").useLines { lineSequence ->
            val duration = measureTime {
                val result = part2(lineSequence)
                assertThat(result).isEqualTo(86879020)
            }
            println(duration)
        }
    }

    private fun part2(input: Sequence<String>): Int {
        val lines = input.toList()
        var sum = 0
        lines.forEachIndexed { row, line ->
            line.forEachIndexed { col, c ->
                if (c.code == '*'.code) { // is Star?
                    // are surrounded by numbers?
                    if (lines.countSurroundNumbers(row, col) == 2) {
                        val pair = lines.takeSurroundNumbers(row, col)
                        sum += (pair.first * pair.second)
                    }
                }
            }
        }
        return sum
    }

    // 1 2 3
    // 4 c 5
    // 6 7 8
    //
    // row--; col--
    private fun List<String>.pos1IsDigit(row: Int, col: Int): Boolean {
        if (row > 0 && col > 0) { // skip first row and first column
            return this[row.dec()][col.dec()].isDigit()
        }
        return false
    }

    // row--; col
    private fun List<String>.pos2IsDigit(row: Int, col: Int): Boolean {
        if (row > 0) { // skip first row
            return this[row.dec()][col].isDigit()
        }
        return false
    }

    // row--; col++
    private fun List<String>.pos3IsDigit(row: Int, col: Int): Boolean {
        if (row > 0) { // skip first row
            val line = this[row.dec()]
            if (col < line.length - 1) { // skip last column
                return line[col.inc()].isDigit()
            }
        }
        return false
    }

    // row; col--
    private fun List<String>.pos4IsDigit(row: Int, col: Int): Boolean {
        if (col > 0) { // skip first col
            return this[row][col.dec()].isDigit()
        }
        return false
    }

    // row; col++
    private fun List<String>.pos5IsDigit(row: Int, col: Int): Boolean {
        val line = this[row]
        if (col < line.length - 1) { //  skip last col
            return line[col.inc()].isDigit()
        }
        return false
    }

    // row++; col--
    private fun List<String>.pos6IsDigit(row: Int, col: Int): Boolean {
        if (row < this.size - 1 && col > 0) {
            return this[row.inc()][col.dec()].isDigit()
        }
        return false
    }

    // row++; col
    private fun List<String>.pos7IsDigit(row: Int, col: Int): Boolean {
        if (row < this.size - 1) {
            return this[row.inc()][col].isDigit()
        }
        return false
    }

    // row++; col++
    private fun List<String>.pos8IsDigit(row: Int, col: Int): Boolean {
        if (row < this.size - 1) {
            val line = this[row.inc()]
            if (col < line.length - 1) {
                return line[col.inc()].isDigit()
            }
        }
        return false
    }

    private fun List<String>.surroundedByAnyNumber(row: Int, col: Int): Boolean {
        if (this.pos1IsDigit(row, col)) {
            return true
        }
        if (this.pos2IsDigit(row, col)) {
            return true
        }
        if (this.pos3IsDigit(row, col)) {
            return true
        }
        if (this.pos4IsDigit(row, col)) {
            return true
        }
        if (this.pos5IsDigit(row, col)) {
            return true
        }
        if (this.pos6IsDigit(row, col)) {
            return true
        }
        if (this.pos7IsDigit(row, col)) {
            return true
        }
        if (this.pos8IsDigit(row, col)) {
            return true
        }
        return false
    }

    private fun List<String>.countSurroundNumbers(row: Int, col: Int): Int {
        val one = this.pos1IsDigit(row, col)
        val two = this.pos2IsDigit(row, col)
        val three = this.pos3IsDigit(row, col)
        val count1 = when {
            one == false && two == false && three == false -> 0
            one == true && two == false && three == false -> 1
            one == false && two == true && three == false -> 1
            one == false && two == false && three == true -> 1
            one == true && two == true && three == false -> 1
            one == false && two == true && three == true -> 1
            one == true && two == false && three == true -> 2
            one == true && two == true && three == true -> 1
            else -> error("Shouldn't happen was $one $two $three")
        }
        val four = this.pos4IsDigit(row, col)
        val five = this.pos5IsDigit(row, col)
        val count2 = when {
            four == false && five == false -> 0
            four == true && five == false -> 1
            four == false && five == true -> 1
            four == true && five == true -> 2
            else -> error("Shouldn't happen was $four $five")
        }

        val six = this.pos6IsDigit(row, col)
        val seven = this.pos7IsDigit(row, col)
        val eight = this.pos8IsDigit(row, col)
        val count3 = when {
            six == false && seven == false && eight == false -> 0
            six == true && seven == false && eight == false -> 1
            six == false && seven == true && eight == false -> 1
            six == false && seven == false && eight == true -> 1
            six == true && seven == true && eight == false -> 1
            six == false && seven == true && eight == true -> 1
            six == true && seven == false && eight == true -> 2
            six == true && seven == true && eight == true -> 1
            else -> error("Shouldn't happen was $six $seven $eight")
        }
        return count1 + count2 + count3
    }

    private fun List<String>.takeSurroundNumbers(row: Int, col: Int): Pair<Int, Int> {
        val numbers = mutableListOf<Int>()

        val one = this.pos1IsDigit(row, col)
        val two = this.pos2IsDigit(row, col)
        val three = this.pos3IsDigit(row, col)
        val count1 = when {
            one == false && two == false && three == false -> 0
            one == true && two == false && three == false -> numbers.add(takeBackward(row.dec(), col.dec()))
            one == false && two == true && three == false -> numbers.add(takeBackward(row.dec(), col))
            one == false && two == false && three == true -> numbers.add(takeForward(row.dec(), col.inc()))
            one == true && two == true && three == false -> numbers.add(takeBackward(row.dec(), col))
            one == false && two == true && three == true -> numbers.add(takeForward(row.dec(), col))
            one == true && two == false && three == true -> {
                numbers.add(takeBackward(row.dec(), col.dec()))
                numbers.add(takeForward(row.dec(), col.inc()))
            }

            one == true && two == true && three == true -> numbers.add(takeForward(row.dec(), col.dec()))
            else -> error("Shouldn't happen was $one $two $three")
        }
        val four = this.pos4IsDigit(row, col)
        val five = this.pos5IsDigit(row, col)
        val count2 = when {
            four == false && five == false -> 0
            four == true && five == false -> numbers.add(takeBackward(row, col.dec()))
            four == false && five == true -> numbers.add(takeForward(row, col.inc()))
            four == true && five == true -> {
                numbers.add(takeBackward(row, col.dec()))
                numbers.add(takeForward(row, col.inc()))
            }

            else -> error("Shouldn't happen was $four $five")
        }

        val six = this.pos6IsDigit(row, col)
        val seven = this.pos7IsDigit(row, col)
        val eight = this.pos8IsDigit(row, col)
        val count3 = when {
            six == false && seven == false && eight == false -> 0
            six == true && seven == false && eight == false -> numbers.add(takeBackward(row.inc(), col.dec()))
            six == false && seven == true && eight == false -> numbers.add(takeForward(row.inc(), col))
            six == false && seven == false && eight == true -> numbers.add(takeForward(row.inc(), col.inc()))
            six == true && seven == true && eight == false -> numbers.add(takeBackward(row.inc(), col))
            six == false && seven == true && eight == true -> numbers.add(takeForward(row.inc(), col))
            six == true && seven == false && eight == true -> {
                numbers.add(takeBackward(row.inc(), col.dec()))
                numbers.add(takeForward(row.inc(), col.inc()))
            }

            six == true && seven == true && eight == true -> numbers.add(takeForward(row.inc(), col.dec()))
            else -> error("Shouldn't happen was $six $seven $eight")
        }
        return Pair(numbers[0], numbers[1])
    }

    private fun List<String>.takeBackward(row: Int, col: Int): Int {
        // including current pos, search backward for a number
        val currentNumber = StringBuilder()
        val line = this[row]
        line.slice(0..col).reversed().forEach {
            if (it.isDigit()) {
                currentNumber.append(it)
            } else {
                return currentNumber.reverse().toString().toInt()
            }
        }
        return currentNumber.reverse().toString().toInt()
    }

    private fun List<String>.takeForward(row: Int, col: Int): Int {
        // including current pos, search backward for a number
        val currentNumber = StringBuilder()
        val line = this[row]
        line.substring(col).forEach {
            if (it.isDigit()) {
                currentNumber.append(it)
            } else {
                return currentNumber.toString().toInt()
            }
        }
        return currentNumber.toString().toInt()
    }
}
