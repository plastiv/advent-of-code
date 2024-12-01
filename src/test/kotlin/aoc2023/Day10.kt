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

    fun Position.north() = Position(this.col, this.row - 1)
    fun Position.northEast() = Position(this.col - 1, this.row - 1)
    fun Position.northWest() = Position(this.col + 1, this.row - 1)
    fun Position.east() = Position(this.col - 1, this.row)
    fun Position.west() = Position(this.col + 1, this.row)
    fun Position.south() = Position(this.col, this.row + 1)
    fun Position.southEast() = Position(this.col - 1, this.row + 1)
    fun Position.southWest() = Position(this.col + 1, this.row + 1)

    fun Map<Position, Node>.nearbyWindow(position: Position): List<Node> {
//        val current = requireNotNull(this[position]) { "Shouldn't happen, but $position element is missing at the map" }
        return listOf(
            position.northEast(),
            position.north(),
            position.northWest(),
            position.east(),
            position.west(),
            position.southEast(),
            position.south(),
            position.southWest()
        ).mapNotNull { position ->
            this[position] // naive way to filter out col and row boundaries
        }
    }

    fun Node.isConnectedTo(another: Node) = when (another.position) {
        this.position.north() -> arrayOf('|', '7', 'F').any { it == another.value }
        this.position.south() -> arrayOf('|', 'L', 'J').any { it == another.value }
        this.position.east() -> arrayOf('-', 'L', 'F').any { it == another.value }
        this.position.west() -> arrayOf('-', 'J', '7').any { it == another.value }
        else -> false
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
}
