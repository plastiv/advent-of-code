package aoc2024

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test
import kotlin.time.measureTime

typealias Area = List<Positionm>

class Day12 {
    @Test
    fun part1Example1() {
        val input =
            $"""
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
            $"""
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
        val duration = measureTime {
            val result = part1(lines)
            assertThat(result).isEqualTo(1471452)
        }
        println("part1 $duration")
    }

    fun <T> Map<Positionm, T>.areConnected2(firstPosition: Positionm, another: Positionm): Boolean {
        val firstValue = this[firstPosition]!!
        val secondValue = this[another]!!
        return secondValue == firstValue
    }

    fun <T> Grid<T>.createAreaFrom(startPosition: Positionm, visited: MutableSet<Positionm>): Area {

        val nearbyList = this.nearbyWindow(startPosition)
            .filter { another ->
                this.elements.areConnected2(startPosition, another)
            }.filter { another ->
                // filter out already visited to prevent cycle
                another !in visited
            }
            .map { nearby ->
                visited.add(nearby)
                createAreaFrom(nearby, visited)
            }
            .flatten()
        val newList = nearbyList.toMutableList() + startPosition
//        val newList = nearbyList.toMutableList()
        return newList
    }

    fun <T> Grid<T>.fenceCount(area: Area): Int {
        return area.sumOf { position ->
            4 - this.nearbyWindow(position)
                .filter { another ->
                    this.elements.areConnected2(position, another)
                }.size
        }
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
    fun part2Example() {
        val input =
            $"""
            """.trimIndent()
                .lines()
        val result = part2(input)
        assertThat(result).isEqualTo(0)
    }

    @Test
    fun part2Input() {
        val lines = fileInput("Day12.txt").readLines()
        val duration = measureTime {
            val result = part2(lines)
            assertThat(result).isEqualTo(0)
        }
        println("part2 $duration")
    }

    fun part2(input: List<String>): Int {
        return 0
    }
}
