package aoc2024

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.time.measureTime

class Day18 {
    @Test
    fun part1Example1() {
        val input =
            """
5,4
4,2
4,5
3,0
2,1
6,3
2,4
1,5
0,6
3,3
2,6
5,1
1,2
5,5
2,5
6,5
1,4
0,4
6,4
1,1
6,1
1,0
0,5
1,6
2,0
            """.trimIndent()
                .lines()
        val result = part1(input)
        assertThat(result).isEqualTo(0)
    }

    @Test
    fun part1Input() {
        val lines = fileInput("Day18.txt").readLines()
        val duration =
            measureTime {
                val result = part1(lines)
                assertThat(result).isEqualTo(0)
            }
        println("part1 $duration")
    }

    data class Bounds(
        val vertical: IntRange,
        val horizontal: IntRange,
    )

    fun Bounds.inside(position: Positionm): Boolean = position.row in vertical && position.col in horizontal

    fun part1(input: List<String>): Int {
        val faultSegments =
            input
                .take(1024)
                .map { string ->
                    string.split(',').map(String::toInt)
                }.map { (first, second) -> Positionm(second, first) }

        var visited = mutableSetOf<Positionm>()
        val queue: Queue<Positionm> = LinkedList<Positionm>()
        val parentMap = mutableMapOf<Positionm, Positionm>()
        val start = Positionm(0, 0)
        val end = Positionm(70, 70)
        val bounds = Bounds(IntRange(0, 70), IntRange(0, 70))
        queue.add(start)
        while (queue.isNotEmpty()) {
            val candidate = queue.poll()
            if (candidate in visited) {
                continue
            }
            visited.add(candidate)
            if (candidate == end) {
                break
            }
            val neighbors =
                listOf(
                    candidate.north(),
                    candidate.east(),
                    candidate.south(),
                    candidate.west(),
                ).filter {
                    bounds.inside(it)
                }.filterNot { position ->
                    position in faultSegments
                }

            neighbors.forEach { neighbor ->
                queue.add(neighbor)
                if (!parentMap.contains(neighbor)) {
                    parentMap[neighbor] = candidate
                }
            }
        }
        val shortPath =
            buildList {
                var current = end
                while (current != start) {
                    add(current)
                    current = parentMap[current]!!
                }
            }
        bounds.horizontal
            .map { i ->
                bounds.vertical
                    .map { j ->
                        val pos = Positionm(i, j)
                        if (pos in faultSegments) {
                            '#'
//                        } else if (pos in visited) {
                        } else if (pos in shortPath) {
                            '0'
                        } else {
                            '.'
                        }
                    }.joinToString("")
            }.joinToString("\n")
            .also(::println)
        return shortPath.size
    }

    @Test
    fun part2Example1() {
        val input =
            """

            """.trimIndent()
                .lines()
        val result = part2(input)
        assertThat(result).isEqualTo(117440)
    }

    @Test
    fun part2Input() {
        val lines = fileInput("Day18.txt").readLines()
        val duration =
            measureTime {
                val result = part2(lines)
                assertThat(result).isEqualTo(117440)
            }
        println("part2 $duration")
    }

    fun part2(input: List<String>): Long = 0
}
