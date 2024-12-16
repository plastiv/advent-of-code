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
                assertThat(result).isEqualTo(79404) // 79412 is too high
            }
        println("part1 $duration")
    }

    enum class Direction {
        N,
        E,
        S,
        W,
    }

    fun Direction.others(): List<Direction> =
        when (this) {
            Direction.N -> listOf(Direction.E, Direction.S, Direction.W)
            Direction.E -> listOf(Direction.N, Direction.S, Direction.W)
            Direction.S -> listOf(Direction.N, Direction.E, Direction.W)
            Direction.W -> listOf(Direction.N, Direction.E, Direction.S)
        }

    fun Positionm.neighborsWithWeight(d1: Direction): List<Pair<Pair<Positionm, Direction>, Int>> =
        d1
            .others()
            .map { d2 ->
                (this to d2) to d1.weight(d2)
            }

    fun Positionm.neighbors(d1: Direction): List<Pair<Positionm, Direction>> =
        d1
            .others()
            .map { d2 ->
                (this to d2)
            }

    fun Direction.weight(direction: Direction): Int =
        when (Pair(this, direction)) {
            Direction.N to Direction.E -> 1000
            Direction.N to Direction.S -> 0
            Direction.N to Direction.W -> 1000
            Direction.E to Direction.S -> 1000
            Direction.E to Direction.N -> 1000
            Direction.E to Direction.W -> 0
            Direction.S to Direction.W -> 1000
            Direction.S to Direction.N -> 0
            Direction.S to Direction.E -> 1000
            Direction.W to Direction.N -> 1000
            Direction.W to Direction.E -> 0
            Direction.W to Direction.S -> 1000
            else -> error("shouldn't happen, but was ${Pair(this, direction)}")
        }

    fun Grid<Char>.dijkstraWithLoops(start: Positionm): Map<Pair<Positionm, Direction>, Int> {
        val distances = mutableMapOf<Pair<Positionm, Direction>, Int>().withDefault { Int.MAX_VALUE }
        val priorityQueue = PriorityQueue<Pair<Pair<Positionm, Direction>, Int>>(compareBy { it.second })
        val visited = mutableSetOf<Pair<Positionm, Direction>>()

        priorityQueue.add((start to Direction.E) to 0)
        distances[start to Direction.E] = 0

        while (priorityQueue.isNotEmpty()) {
            val (node, currentDist) = priorityQueue.poll()
            val (position, direction) = node
            if (visited.add(position to direction)) {
                val neighbors =
                    buildList {
                        addAll(position.neighborsWithWeight(direction))
                        val (neighborP, neighborD) =
                            when (direction) {
                                Direction.N -> position.north() to Direction.S
                                Direction.E -> position.east() to Direction.W
                                Direction.S -> position.south() to Direction.N
                                Direction.W -> position.west() to Direction.E
                            }
                        if (this@dijkstraWithLoops.elements[neighborP] != '#') {
                            add(Pair(Pair(neighborP, neighborD), 1))
                        }
                    }

                neighbors
                    .forEach { (node, weight) ->
                        val (adjacentP, adjacentD) = node
                        val totalDist = currentDist + weight
                        if (totalDist < distances.getValue(adjacentP to adjacentD)) {
                            distances[adjacentP to adjacentD] = totalDist
                            priorityQueue.add((adjacentP to adjacentD) to totalDist)
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

        return shortDistances
            .filter { entry ->
                entry.key.first == end
            }.minOf { entry ->
                entry.value
            }
    }

    @Test
    fun part2Example1() {
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
        val result = part2(input)
        assertThat(result).isEqualTo(45)
    }

    @Test
    fun part2Example2() {
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
        val result = part2(input)
        assertThat(result).isEqualTo(64)
    }

    @Test
    fun part2Input() {
        val lines = fileInput("Day16.txt").readLines()
        val duration =
            measureTime {
                val result = part2(lines)
                assertThat(result).isEqualTo(0) // 464 is too high
                // 463 other person input
            }
        println("part2 $duration")
    }

    fun Map<Pair<Positionm, Direction>, Int>.countAllShorts(start: Positionm): Set<Pair<Positionm, Direction>> {
        var visited = mutableSetOf<Pair<Positionm, Direction>>()
        val queue: Queue<Pair<Positionm, Direction>> = LinkedList<Pair<Positionm, Direction>>()
//        queue.add(start to Direction.N)
//        queue.add(start to Direction.E)
        queue.add(start to Direction.S)
//        queue.add(start to Direction.W)
        while (queue.isNotEmpty()) {
            val (position, direction) = queue.poll()
            val nodeDistance = this.getOrDefault(position to direction, Int.MAX_VALUE)
            // find neighbors
            val neighbors =
                buildList {
                    position
                        .neighbors(direction)
                        .filter { this@countAllShorts.containsKey(it) }
                        .filterNot { it in visited }
                        .forEach { add(it) }
                    val neighbor =
                        when (direction) {
                            Direction.N -> position.north() to Direction.S
                            Direction.E -> position.east() to Direction.W
                            Direction.S -> position.south() to Direction.N
                            Direction.W -> position.west() to Direction.E
                        }
                    if (this@countAllShorts.containsKey(neighbor)) {
                        add(neighbor)
                    }
                }

            neighbors.forEach { candidate ->
                val distance = this.getOrDefault(candidate, Int.MAX_VALUE)
                // filter distance less than current
                if (distance <= nodeDistance) {
                    visited.add(candidate)
                    queue.add(candidate)
                }
            }
        }
        return visited
    }

    fun part2(input: List<String>): Int {
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

//        grid
//            .rowIndices()
//            .joinToString("\n") { i ->
//                grid
//                    .columnIndices()
//                    .map { j ->
//                        val distance = shortDistances[Positionm(i, j)]
//                        return@map if (distance == null) {
//                            "    "
//                        } else {
//                            val displayDistance = distance
//                            val distanceStr = displayDistance.toString()
//                            when (distanceStr.length) {
//                                1 -> "   $distanceStr"
//                                2 -> "  $distanceStr"
//                                3 -> " $distanceStr"
//                                4 -> "$distanceStr"
// //                                4 -> " $distanceStr"
// //                                5 -> "$distanceStr"
//                                else -> error("shouldn't happen, but was $distanceStr")
//                            }
//                        }
//                    }.joinToString("")
//            }.also(::println)

        val pathElements = shortDistances.countAllShorts(end)

        val onlyPositions = pathElements.map { it.first }.toSet()
        grid
            .rowIndices()
            .joinToString("\n") { i ->
                grid
                    .columnIndices()
                    .map { j ->
                        if (onlyPositions.contains(Positionm(i, j))) {
                            "0"
                        } else {
                            when (grid.elements[Positionm(i, j)]) {
                                '#' -> '■'
                                '.' -> ' '
                                else -> grid.elements[Positionm(i, j)]
                            }
                        }
                    }.joinToString("")
            }.also(::println)

        pathElements
            .map { pair ->
                "${shortDistances[pair]} ${pair.first.row},${pair.first.col},${pair.second}"
            }.also(::println)

        return onlyPositions.size
    }
}
