package aoc2024

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.time.measureTime

class Day16 {
    @Test
    fun part1Example1() {
        val input =
            """
###############
#.......#....E#
#.#.###.#.###.#
#.....#.#...#.#
#.###.#####.#.#
#.#.#.......#.#
#.#.#####.###.#
#...........#.#
###.#.#####.#.#
#...#.....#.#.#
#.#.#.###.#.#.#
#.....#...#.#.#
#.###.#.#.#.#.#
#S..#.....#...#
###############
            """.trimIndent()
                .lines()
        val result = part1(input)
        assertThat(result).isEqualTo(7036)
    }

    @Test
    fun part1Example2() {
        val input =
            """
#################
#...#...#...#..E#
#.#.#.#.#.#.#.#.#
#.#.#.#...#...#.#
#.#.#.#.###.#.#.#
#...#.#.#.....#.#
#.#.#.#.#.#####.#
#.#...#.#.#.....#
#.#.#####.#.###.#
#.#.#.......#...#
#.#.###.#####.###
#.#.#...#.....#.#
#.#.#.#####.###.#
#.#.#.........#.#
#.#.#.#########.#
#S#.............#
#################
            """.trimIndent()
                .lines()
        val result = part1(input)
        assertThat(result).isEqualTo(11048)
    }

    @Test
    fun part1Input() {
        val lines = fileInput("Day16.txt").readLines()
        val duration =
            measureTime {
                val result = part1(lines)
                assertThat(result).isEqualTo(0) // 79412 is too high
            }
        println("part1 $duration")
    }

    enum class Direction {
        N,
        E,
        S,
        W,
    }

    fun Grid<Char>.dijkstraWithLoops(start: Positionm): Map<Positionm, Int> {
        val distances = mutableMapOf<Positionm, Int>().withDefault { Int.MAX_VALUE }
        val priorityQueue = PriorityQueue<Triple<Positionm, Int, Direction>>(compareBy { it.second })
        val visited = mutableSetOf<Pair<Positionm, Int>>()

        priorityQueue.add(Triple(start, 0, Direction.E)) // TODO: East
        distances[start] = 0

        while (priorityQueue.isNotEmpty()) {
            val (node, currentDist, currentDirection) = priorityQueue.poll()
            if (visited.add(node to currentDist)) {
                this
                    .neighbors4(node)
                    .filterNot { this.elements[it] == '#' }
                    .forEach { adjacent ->
                        val adjDirection =
                            when (adjacent) {
                                node.north() -> Direction.N
                                node.east() -> Direction.E
                                node.south() -> Direction.S
                                node.west() -> Direction.W
                                else -> error("shouldn't happen, but was $adjacent")
                            }
                        val weight = if (currentDirection == adjDirection) 1 else 1001
                        val totalDist = currentDist + weight
                        if (totalDist < distances.getValue(adjacent)) {
                            distances[adjacent] = totalDist
                            priorityQueue.add(Triple(adjacent, totalDist, adjDirection))
                        }
                    }
            }
        }
        return distances
    }

    fun part1(input: List<String>): Int {
        val grid =
            input
                .toCharGrid()
                .also(::println)

        val start =
            grid.elements
                .firstNotNullOf { entry ->
                    if (entry.value == 'S') entry.key else null
                }.also(::println)

        val end =
            grid.elements
                .firstNotNullOf { entry ->
                    if (entry.value == 'E') entry.key else null
                }.also(::println)
        val shortDistances =
            grid
                .dijkstraWithLoops(start)
                .also(::println)

        grid
            .rowIndices()
            .joinToString("\n") { i ->
                grid
                    .columnIndices()
                    .map { j ->
                        val distance = shortDistances[Positionm(i, j)]
                        return@map if (distance == null) {
                            "  "
                        } else {
                            val displayDistance = distance / 1000
                            val distanceStr = displayDistance.toString()
                            when (distanceStr.length) {
                                1 -> " $distanceStr"
                                2 -> "$distanceStr"
                                3 -> "XX"
//                                4 -> " $distanceStr"
//                                5 -> "$distanceStr"
                                else -> error("shouldn't happen, but was $distanceStr")
                            }
                        }
                    }.joinToString("")
            }.also(::println)
        return shortDistances[end]!!
    }

    @Test
    fun part2Example1() {
        val input =
            """"""
                .trimIndent()
                .lines()
        val result = part2(input)
        assertThat(result).isEqualTo(0)
    }

    @Test
    fun part2Input() {
        val lines = fileInput("Day15.txt").readLines()
        val duration =
            measureTime {
                val result = part2(lines)
                assertThat(result).isEqualTo(0)
            }
        println("part2 $duration")
    }

    fun part2(input: List<String>): Int = 0
}
