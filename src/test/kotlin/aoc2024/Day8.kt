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
        if (this == another) {
            return this
        }
        // A, B
        // C = A + ( (B-A) * 2 )
        val newRow = this.row + (another.row - this.row) * (1 + step)

        val newCol = this.col + (another.col - this.col) * (1 + step)

        return Positionm(newRow, newCol)
    }

    fun part1(input: List<String>): Int {
        val antinodes = mutableSetOf<Positionm>()
        val map = input.toCharGrid()

        map.elements.filter { entry -> entry.value != '.' }
            .entries
            .map { entry -> Element(entry.key, entry.value) }
            .groupBy { element -> element.char }
            .map { entry ->
                val frequencies = entry.value.map { element -> element.position }

                findPairs(frequencies)
                    .forEach { (a, b) ->
                        val candidate1 = a.invert(b)
                        if (map.inBounds(candidate1)) {
                            antinodes.add(candidate1)
                        }

                        val candidate2 = b.invert(a)
                        if (map.inBounds(candidate2)) {
                            antinodes.add(candidate2)
                        }
                    }
            }
        return antinodes.size
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
        val antinodes = mutableSetOf<Positionm>()
        val map = input.toCharGrid()

        map.elements.filter { entry -> entry.value != '.' }
            .entries
            .map { entry -> Element(entry.key, entry.value) }
            .groupBy { element -> element.char }
            .map { entry ->

                val frequencies = entry.value.map { element -> element.position }

                findAntinodes(map, frequencies, antinodes)
            }

        return antinodes.size
    }

    fun findAntinodes(
        map: Grid<Char>,
        frequencies: List<Positionm>,
        antinodes: MutableSet<Positionm>,
    ) {
        antinodes.addAll(frequencies)
        val pairs = findPairs(frequencies)

        fun findAntinodeCandidates(a: Positionm, b: Positionm, step: Int = 1): List<Positionm> {
            val candidate1 = a.invert(b, step)
            val candidate2 = b.invert(a, step)
            return if (map.inBounds(candidate1) && map.inBounds(candidate2)) {
                listOf(candidate1, candidate2) + findAntinodeCandidates(a, b, step.inc())
            } else if (map.inBounds(candidate1)) {
                listOf(candidate1) + findAntinodeCandidates(a, b, step.inc())
            } else if (map.inBounds(candidate2)) {
                listOf(candidate2) + findAntinodeCandidates(a, b, step.inc())
            } else {
                emptyList()
            }
        }

        for ((a, b) in pairs) {
            val candidates = findAntinodeCandidates(a, b)
            antinodes.addAll(candidates) // deduplicate candidates with Set
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
