package aoc2024

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.math.abs
import kotlin.test.Test
import kotlin.time.measureTime

typealias Area = List<Positionm>

class Day12 {
    @Test
    fun part1Example1() {
        val input =
            """
            AAAA
            BBCD
            BBCC
            EEEC
            """.trimIndent()
                .lines()
        val result = part1(input)
        assertThat(result).isEqualTo(140)
    }

    @Test
    fun part1Example2() {
        val input =
            """
            RRRRIICCFF
            RRRRIICCCF
            VVRRRCCFFF
            VVRCCCJFFF
            VVVVCJJCFE
            VVIVCCJJEE
            VVIIICJJEE
            MIIIIIJJEE
            MIIISIJEEE
            MMMISSJEEE
            """.trimIndent()
                .lines()
        val result = part1(input)
        assertThat(result).isEqualTo(1930)
    }

    @Test
    fun part1Input() {
        val lines = fileInput("Day12.txt").readLines()
        val duration =
            measureTime {
                val result = part1(lines)
                assertThat(result).isEqualTo(1471452)
            }
        println("part1 $duration")
    }

    fun <T> Map<Positionm, T>.areConnected2(
        firstPosition: Positionm,
        another: Positionm,
    ): Boolean {
        val firstValue = this[firstPosition]
        val secondValue = this[another]
        // both can't be null at the same time
        check(firstValue != null || secondValue != null)
        return secondValue == firstValue
    }

    fun <T> Grid<T>.createAreaFrom(
        startPosition: Positionm,
        visited: MutableSet<Positionm>,
    ): Area {
        val nearbyList =
            this
                .nearbyWindow(startPosition)
                .filter { another ->
                    this.elements.areConnected2(startPosition, another)
                }.filter { another ->
                    // filter out already visited to prevent cycle
                    another !in visited
                }.map { nearby ->
                    visited.add(nearby)
                    createAreaFrom(nearby, visited)
                }.flatten()
        val newList = nearbyList.toMutableList() + startPosition
//        val newList = nearbyList.toMutableList()
        return newList
    }

    fun <T> Grid<T>.fenceCount(area: Area): Int =
        area.sumOf { position ->
            4 -
                this
                    .nearbyWindow(position)
                    .filter { another ->
                        this.elements.areConnected2(position, another)
                    }.size
        }

    fun part1(input: List<String>): Int {
        val visited = mutableSetOf<Positionm>()

        val charGrid = input.toCharGrid()
        return buildList {
            charGrid
                .elements
                .forEach {
                    if (it.key !in visited) {
                        add(it.value to charGrid.createAreaFrom(it.key, visited).toSet().toList())
                    }
                }
        }.sumOf { (char, area) ->
            val fenceCount = charGrid.fenceCount(area)
            println("$char size ${area.size} fence $fenceCount")
            fenceCount * area.size
        }
    }

    @Test
    fun part2Example1() {
        val input =
            """
            AAAA
            BBCD
            BBCC
            EEEC
            """.trimIndent()
                .lines()
        val result = part2(input)
        assertThat(result).isEqualTo(80)
    }

    @Test
    fun part2Example2() {
        val input =
            """
            EEEEE
            EXXXX
            EEEEE
            EXXXX
            EEEEE
            """.trimIndent()
                .lines()
        val result = part2(input)
        assertThat(result).isEqualTo(236)
    }

    @Test
    fun part2Example3() {
        val input =
            """
            AAAAAA
            AAABBA
            AAABBA
            ABBAAA
            ABBAAA
            AAAAAA
            """.trimIndent()
                .lines()
        val result = part2(input)
        assertThat(result).isEqualTo(368)
    }

    @Test
    fun part2Example4() {
        val input =
            """
            RRRRIICCFF
            RRRRIICCCF
            VVRRRCCFFF
            VVRCCCJFFF
            VVVVCJJCFE
            VVIVCCJJEE
            VVIIICJJEE
            MIIIIIJJEE
            MIIISIJEEE
            MMMISSJEEE
            """.trimIndent()
                .lines()
        val result = part2(input)
        assertThat(result).isEqualTo(1206)
    }

    @Test
    fun part2Input() {
        val lines = fileInput("Day12.txt").readLines()
        val duration =
            measureTime {
                val result = part2(lines)
                assertThat(result).isEqualTo(863366)
            }
        println("part2 $duration")
    }

    fun List<Positionm>.countContinious(): Int {
        if (size == 1) return 1
        return this
            .windowed(2)
            .count { (first, second) ->
                abs(first.row - second.row) > 1 ||
                    abs(first.col - second.col) > 1
            } + 1
    }

    fun List<Positionm>.countHorizontalEdges(): Int {
        if (size == 1) return 1
        return this
            .groupBy { it.row }
            .values
            .sumOf {
                1 +
                    it
                        .windowed(2)
                        .count { (first, second) ->
                            abs(second.col - first.col) > 1
                        }
            }
    }

    fun List<Positionm>.countVerticalEdges(): Int {
        if (size == 1) return 1
        return this
            .groupBy { it.col }
            .values
            .sumOf {
                1 +
                    it
                        .windowed(2)
                        .count { (first, second) ->
                            abs(second.row - first.row) > 1
                        }
            }
    }

    fun <T> Grid<T>.fenceCount2(area: Area): Int {
        val northFences = mutableSetOf<Positionm>()
        val westFences = mutableSetOf<Positionm>()
        val southFences = mutableSetOf<Positionm>()
        val eastFences = mutableSetOf<Positionm>()
        area.forEach { posInArea ->
            if (!area.contains(posInArea.north())) {
                northFences.add(posInArea.north())
            }
            if (!area.contains(posInArea.west())) {
                westFences.add(posInArea.west())
            }
            if (!area.contains(posInArea.east())) {
                eastFences.add(posInArea.east())
            }
            if (!area.contains(posInArea.south())) {
                southFences.add(posInArea.south())
            }
        }
        check(northFences.isNotEmpty())
        check(westFences.isNotEmpty())
        check(southFences.isNotEmpty())
        check(eastFences.isNotEmpty())

        return northFences.sortedBy { it.row }.countHorizontalEdges() +
            westFences.sortedBy { it.col }.countVerticalEdges() +
            southFences.sortedBy { it.row }.countHorizontalEdges() +
            eastFences.sortedBy { it.col }.countVerticalEdges()
    }

    fun part2(input: List<String>): Int {
        val visited = mutableSetOf<Positionm>()

        val charGrid = input.toCharGrid()
        return buildList {
            charGrid
                .elements
                .forEach {
                    if (it.key !in visited) {
                        add(it.value to charGrid.createAreaFrom(it.key, visited).toSet().toList())
                    }
                }
        }.sumOf { (char, area) ->
            val fenceCount = charGrid.fenceCount2(area.sortedWith(compareBy<Positionm>({ it.row }, { it.col })))
            println("$char size ${area.size} fence $fenceCount")
            fenceCount * area.size
        }
    }
}
