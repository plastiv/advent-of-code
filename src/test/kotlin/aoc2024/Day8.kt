package aoc2024

import assertk.assertThat
import assertk.assertions.isEqualTo
import utils.head
import utils.tail
import kotlin.math.abs
import kotlin.test.Test
import kotlin.time.measureTime

class Day8 {
    @Test
    fun part1Example() {
        val input =
            $"""
............
........0...
.....0......
.......0....
....0.......
......A.....
............
............
........A...
.........A..
............
............
            """.trimIndent()
                .lines()
        val result = part1(input)
        assertThat(result).isEqualTo(14)
    }

    @Test
    fun part1Input() {
        val lines = fileInput("Day8.txt").readLines()
        val duration = measureTime {
            val result = part1(lines)
            assertThat(result).isEqualTo(308)
        }
        println("part1 $duration")
    }

    data class Element(val position: Positionm, val char: Char)

    fun Positionm.invert(another: Positionm, step: Int = 1): Positionm {
        val newRow = if (this.row == another.row) {
            // we only flip vertically
            this.row
        } else if (this.row < another.row) {
            // this minus another
            val rowDistance = abs(another.row - this.row) * step
            this.row - rowDistance
        } else {
            // this.row > another.row
            // this add another
            val rowDistance = abs(another.row - this.row) * step
            this.row + rowDistance
        }


        val newCol = if (this.col == another.col) {
            // we only flip horizontally
            this.col
        } else if (this.col < another.col) {
            // this minus another
            val columnDistance = abs(another.col - this.col) * step
            this.col - columnDistance
        } else {
            // this.col > another.col
            // this add another
            val columnDistance = abs(another.col - this.col) * step
            this.col + columnDistance
        }

        return Positionm(newCol, newRow)

    }

    fun part1(input: List<String>): Int {
        val frequencies = mutableMapOf<Positionm, Char>()
        val map = input.toCharGrid()
        println("Map")
        printMap(map, frequencies)

        map.elements.filter { entry -> entry.value != '.' }
            .entries
            .map { entry -> Element(entry.key, entry.value) }
            .groupBy { element -> element.char }
            .map { entry ->
                val positions = entry.value.map { element -> element.position }

                findPairs(positions)
                    .forEach { (a, b) ->
                        val candidate1 = a.invert(b)
                        if (map.inBounds(candidate1)) {
                            frequencies.put(candidate1, '#')
                        }
                        val candidate2 = b.invert(a)

                        if (map.inBounds(candidate2)) {
                            frequencies.put(candidate2, '#')
                        }
                    }
            }
        println("Freqs")
        printMap(map, frequencies)
        return frequencies.size
    }

    fun printMap(elements: Grid<Char>, reqs: Map<Positionm, Char>) {
        println(elements.columIndices().joinToString("", transform = Int::toString))

        elements.rowIndices().map { i ->
            elements.columIndices().map { j ->
                if (reqs.containsKey(Positionm(j, i))) {
                    '#'
                } else {
                    elements.elements[Positionm(j, i)]
                }
            }.joinToString(separator = "", prefix = "$i", postfix = "$i").also(::println)
        }
    }

    @Test
    fun part2Example() {
        val input =
            $"""
            ............
            ........0...
            .....0......
            .......0....
            ....0.......
            ......A.....
            ............
            ............
            ........A...
            .........A..
            ............
            ............
            """.trimIndent()
                .lines()
        val result = part2(input)
        assertThat(result).isEqualTo(34)
    }

    @Test
    fun part2Example2() {
        val input =
            $"""
            T.........
            ...T......
            .T........
            ..........
            ..........
            ..........
            ..........
            ..........
            ..........
            ..........
            """.trimIndent()
                .lines()
        val result = part2(input)
        assertThat(result).isEqualTo(9)
    }

    @Test
    fun part2Input() {
        val lines = fileInput("Day8.txt").readLines()
        val duration = measureTime {
            val result = part2(lines)
            assertThat(result).isEqualTo(1147)
        }
        println("part2 $duration")
    }

    fun part2(input: List<String>): Int {
        val frequencies = mutableMapOf<Positionm, Char>()
        val map = input.toCharGrid()
        println("Map")
        printMap(map, frequencies)

        map.elements.filter { entry -> entry.value != '.' }
            .entries
            .map { entry -> Element(entry.key, entry.value) }
            .groupBy { element -> element.char }
            .map { entry ->

                val positions = entry.value.map { element -> element.position }

                findFreqs(map, frequencies, positions)
            }

        println("Freqs")
        printMap(map, frequencies)
        return frequencies.size
    }

    fun findFreqs(
        map: Grid<Char>,
        frequencies: MutableMap<Positionm, Char>,
        positions: List<Positionm>,
    ) {
        for (position in positions) {
            frequencies.put(position, '#')
        }
        val pairs = findPairs(positions)

        fun getNextCandidates(a: Positionm, b: Positionm, step: Int = 1): List<Positionm> {
            val candidate = a.invert(b, step)
            return if (map.inBounds(candidate)) {
                listOf(candidate) + getNextCandidates(a, b, step.inc())
            } else {
                emptyList()
            }
        }
        // get all position pairs
        // check each pair
        for ((a, b) in pairs) {
            val candidates = getNextCandidates(a, b)
            val candidates2 = getNextCandidates(b, a)
            for (candidate in candidates + candidates2) {

                val existed = frequencies.put(candidate, '#')
                if (existed == null) {
                    println("Adding c $candidate")
                    printMap(map, frequencies)
                }
            }
        }
    }

    fun findPairs(positions: List<Positionm>): List<Pair<Positionm, Positionm>> {
        if (positions.isEmpty()) return emptyList()
        if (positions.size == 1) error("Shouldn't happen, but was size == 1")
        if (positions.size == 2) return listOf(Pair(positions.first(), positions[1]))
        val head = positions.head()
        val acc = mutableListOf<Pair<Positionm, Positionm>>()
        for (position in positions.tail()) {
            acc.add(Pair(head, position))
        }
        return acc + findPairs(positions.tail())
    }
}
