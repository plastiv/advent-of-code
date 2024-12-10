package aoc2024

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test
import kotlin.time.measureTime

class Day10 {
    @Test
    fun part1Example() {
        val input =
            $"""
89010123
78121874
87430965
96549874
45678903
32019012
01329801
10456732
            """.trimIndent()
                .lines()
        val result = part1(input)
        assertThat(result).isEqualTo(36)
    }

    @Test
    fun part1Input() {
        val lines = fileInput("Day10.txt").readLines()
        val duration = measureTime {
            val result = part1(lines)
            assertThat(result).isEqualTo(6430446922192)
        }
        println("part1 $duration")
    }

//    data class Nodem(val position: Position, val value: Char)

    fun Map<Positionm, Int>.nearbyWindow(position: Positionm): List<Positionm> {
        return listOf(
            position.north(),
            position.east(),
            position.west(),
            position.south(),
        ).mapNotNull { position ->
            return@mapNotNull if (this[position] != null) {
                position
            } else {
                null
            }
        }
    }

    fun Map<Positionm, Int>.isConnectedTo(firstPosition: Positionm, another: Positionm): Boolean {
        val firstValue = this[firstPosition]!!
        val secondValue = this[another]!!
        return secondValue - firstValue == 1
    }

    fun part1(input: List<String>): Int {
        val grid = input.toIntGrid()
        return grid.elements.filterValues { it == 0 }
            .also(::println)
            .map { (position, _) ->
                val visited = mutableMapOf<Positionm, Boolean>()
                val queue = ArrayDeque<Positionm>()
                val start = position
                val peaks = mutableListOf<Positionm>()
                queue.add(start)

                while (queue.isNotEmpty()) {

                    val current = queue.removeFirst()
                    visited.put(current, true)

                    grid.elements.nearbyWindow(current)
                        .filter { another ->
                            grid.elements.isConnectedTo(current, another)
                        }.filter { another ->
                            // filter out already visited to prevent cycle
                            visited[another] != true
                        }
                        .forEach { nearby ->
                            val nearbyValue = grid.elements[nearby]!!
                            if (nearbyValue == 9) {
                                // found
                                visited.put(nearby, true)
                                peaks.add(nearby)
                            }else {
                                queue.add(nearby)
                            }
                        }
                }
                println("For 0 at $position 9 was ${peaks.size}")
                return@map peaks.size
            }.sum()
    }

    @Test
    fun part2Example() {
        val input =
            $"""

            """.trimIndent()
                .lines()
        val result = part2(input)
        assertThat(result).isEqualTo(2858)
    }

    @Test
    fun part2Input() {
        val lines = fileInput("Day10.txt").readLines()
        val duration = measureTime {
            val result = part2(lines)
            assertThat(result).isEqualTo(6460170593016)
        }
        println("part2 $duration")
    }

    fun part2(input: List<String>): Long {
        return 0
    }
}
