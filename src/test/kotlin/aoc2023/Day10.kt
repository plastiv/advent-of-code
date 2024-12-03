package aoc2023

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test
import kotlin.time.measureTime

class Day10 {
    @Test
    fun part1Example1() {
        val input =
            $"""
            .....
            .S-7.
            .|.|.
            .L-J.
            .....
            """.trimIndent()
                .lines()
        val result = part1(input)
        assertThat(result).isEqualTo(4)
    }

    @Test
    fun part1Example2() {
        val input =
            $"""
            ..F7.
            .FJ|.
            SJ.L7
            |F--J
            LJ...
            .....
            """.trimIndent()
                .lines()
        val result = part1(input)
        assertThat(result).isEqualTo(8)
    }

    @Test
    fun part1Input() {
        val lines = fileInput("Day10.txt").readLines()
        val duration = measureTime {
            val result = part1(lines)
            assertThat(result).isEqualTo(7005)
        }
        println("part1 $duration")
    }

    data class Position(val col: Int, val row: Int)
    data class Node(val position: Position, val value: Char)

    // 1 2 3
    // 4 S 5
    // 6 7 8

    fun Position.north() = copy(col, row.dec())
    fun Position.east() = copy(col.dec(), row)
    fun Position.west() = copy(col.inc(), row)
    fun Position.south() = copy(col, row.inc())

    fun Map<Position, Node>.nearbyWindow(position: Position): List<Node> {
        return listOf(
            position.north(),
            position.east(),
            position.west(),
            position.south(),
        ).mapNotNull { position ->
            this[position] // naive way to filter out col and row boundaries
        }
    }

    fun Node.isConnectedTo(another: Node) = when (another.position) {
        position.north() -> another.value in arrayOf('|', '7', 'F', 'S') && value in arrayOf('|', 'L', 'J', 'S')
        position.south() -> another.value in arrayOf('|', 'L', 'J', 'S') && value in arrayOf('|', '7', 'F', 'S')
        position.east() -> another.value in arrayOf('-', 'L', 'F', 'S') && value in arrayOf('-', 'J', '7', 'S')
        position.west() -> another.value in arrayOf('-', 'J', '7', 'S') && value in arrayOf('-', 'L', 'F', 'S')
        else -> error("Shouldn't happen, but was $another")
    }

    fun part1(input: List<String>): Int {
        val elements = input.mapIndexed { row, str ->
            str.mapIndexed { col, char ->
                Node(Position(col, row), char)
            }
        }.flatten()
            .associateBy { it.position }

        val visited = mutableMapOf<Position, Boolean>()
        val pathLength = mutableMapOf<Position, Int>()
        val queue = ArrayDeque<Node>()

        val start = elements.values.first { node -> node.value == 'S' }

        queue.add(start)
        pathLength.put(start.position, 0)

        while (queue.isNotEmpty()) {

            val current = queue.removeFirst()
            visited.put(current.position, true)
            val length = pathLength[current.position]
            println("Visit: ${current.value} ${current.position} for $length")

            elements.nearbyWindow(current.position)
                .filter { another ->
                    current.isConnectedTo(another)
                }.filter { another ->
                    // filter out already visited to prevent cycle
                    visited[another.position] != true
                }
                .forEach { nearby ->
                    queue.add(nearby)
                    val currLength = pathLength[current.position]!!
                    pathLength.put(nearby.position, currLength.inc())
                }
        }

        return pathLength.values.max()
    }

    @Test
    fun part2Example1() {
        val input =
            $"""
            ...........
            .S-------7.
            .|F-----7|.
            .||OOOOO||.
            .||OOOOO||.
            .|L-7OF-J|.
            .|II|O|II|.
            .L--JOL--J.
            .....O.....
            """.trimIndent()
                .lines()
        val result = part2(input)
        assertThat(result).isEqualTo(4)
    }

    @Test
    fun part2Example2() {
        val input =
            $"""
            ..........
            .S------7.
            .|F----7|.
            .||OOOO||.
            .||OOOO||.
            .|L-7F-J|.
            .|II||II|.
            .L--JL--J.
            ..........
            """.trimIndent()
                .lines()
        val result = part2(input)
        assertThat(result).isEqualTo(4)
    }

    @Test
    fun part2Example3() {
        val input =
            $"""
            .F----7F7F7F7F-7....
            .|F--7||||||||FJ....
            .||.FJ||||||||L7....
            FJL7L7LJLJ||LJ.L-7..
            L--J.L7...LJS7F-7L7.
            ....F-J..F7FJ|L7L7L7
            ....L7.F7||L7|.L7L7|
            .....|FJLJ|FJ|F7|.LJ
            ....FJL-7.||.||||...
            ....L---J.LJ.LJLJ...
            """.trimIndent()
                .lines()
        val result = part2(input)
        assertThat(result).isEqualTo(8)
    }

    @Test
    fun part2Example4() {
        val input =
            $"""
            FF7FSF7F7F7F7F7F---7
            L|LJ||||||||||||F--J
            FL-7LJLJ||||||LJL-77
            F--JF--7||LJLJ7F7FJ-
            L---JF-JLJ.||-FJLJJ7
            |F|F-JF---7F7-L7L|7|
            |FFJF7L7F-JF7|JL---7
            7-L-JL7||F7|L7F-7F7|
            L.L7LFJ|||||FJL7||LJ
            L7JLJL-JLJLJL--JLJ.L
            """.trimIndent()
                .lines()
        val result = part2(input)
        assertThat(result).isEqualTo(10)
    }

    @Test
    fun part2Input() {
        val lines = fileInput("Day10.txt").readLines()

        val duration = measureTime {
            val result = part2(lines)
            assertThat(result).isEqualTo(417)
        }
        println("part2 $duration")
    }

    fun Char.prettyPrint() = when (this) {
        'F' -> '┌'
        '|' -> '│'
        '-' -> '─'
        'L' -> '└'
        '7' -> '┐'
        'J' -> '┘'
        else -> this
    }

    fun part2(input: List<String>): Int {
        println(input.joinToString("\n") { string ->
            string.map { it.prettyPrint() }.joinToString("")
        })

        val elements = input.mapIndexed { row, str ->
            str.mapIndexed { col, char ->
                Node(Position(col, row), char)
            }
        }.flatten()
            .associateBy { it.position }

        val visited = mutableMapOf<Position, Boolean>()
        val queue = ArrayDeque<Node>()

        val start = elements.values.first { node -> node.value == 'S' }

        val loopPath = mutableListOf<Position>()
        queue.add(start)
        loopPath.add(start.position)

        while (queue.isNotEmpty()) {

            val current = queue.removeFirst()
            visited.put(current.position, true)

            elements.nearbyWindow(current.position)
                .filter { another ->
                    current.isConnectedTo(another)
                }.filter { another ->
                    // filter out already visited to prevent cycle
                    visited[another.position] != true
                }
                .forEach { nearby ->
                    queue.addFirst(nearby)
                    loopPath.add(nearby.position)
                }
        }

        val colN = input[0].indices
        val rowN = input.indices

        // ray tracing
        val enclosed = mutableMapOf<Position, Boolean>()
        for (row in rowN) {
            var counter = 0
            for (col in colN) {
                val position = Position(col, row)
                if (position in loopPath) {
                    // we trace ray under the polygon edge
                    if (elements[position]!!.value !in arrayOf('-', 'L', 'J')) {
                        counter++
                    }
                    enclosed.put(position, false)
                } else if (counter > 0 && counter % 2 == 1) {
                    enclosed.put(position, true)
                } else {
                    enclosed.put(position, false)
                }
            }
        }

        return enclosed.count { entry -> entry.value }
    }
}
