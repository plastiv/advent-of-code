package aoc2024

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test
import utils.binarySearchLowerBound
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
        val result = part1(input, 12, 6, 6)
        assertThat(result).isEqualTo(22)
    }

    @Test
    fun part1Input() {
        val lines = fileInput("Day18.txt").readLines()
        val duration =
            measureTime {
                val result = part1(lines, 1024, 70, 70)
                assertThat(result).isEqualTo(404)
            }
        println("part1 $duration")
    }

    data class Bounds(
        val vertical: IntRange,
        val horizontal: IntRange,
    )

    fun Bounds.inside(position: Positionm): Boolean = position.row in vertical && position.col in horizontal

    fun part1(
        input: List<String>,
        iter: Int,
        width: Int,
        height: Int,
    ): Int {
        val faultSegments =
            input
                .map { string ->
                    string.split(',').map(String::toInt)
                }.map { (first, second) -> Positionm(second, first) }

        val path = findPath(faultSegments, iter, width, height)
        return path.size
    }

    @Test
    fun part2Example1() {
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
        val result = part2(input, 6, 6)
        assertThat(result).isEqualTo(Positionm(1, 6))
    }

    @Test
    fun part2Input() {
        val lines = fileInput("Day18.txt").readLines()
        val duration =
            measureTime {
                val result = part2(lines, 70, 70)
                assertThat(result).isEqualTo(Positionm(60, 27))
            }
        println("part2 $duration")
    }

    fun findPath(
        allFaults: List<Positionm>,
        takeN: Int,
        width: Int = 70,
        height: Int = 70,
    ): List<Positionm> {
        val faultSegments = allFaults.take(takeN)
        var visited = mutableSetOf<Positionm>()
        val queue: Queue<Positionm> = LinkedList<Positionm>()
        val parentMap = mutableMapOf<Positionm, Positionm>()
        val start = Positionm(0, 0)
        val end = Positionm(height, width)
        val bounds = Bounds(IntRange(0, height), IntRange(0, width))
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

        println("")
        println("iter $takeN ${allFaults[takeN]}")
        println("")
        if (end !in parentMap) {
            return emptyList()
        }
        val shortPath =
            buildList {
                var current = end
                while (current != start) {
                    add(current)
                    current = parentMap[current]!!
                }
            }

        return shortPath
    }

    fun part2(
        input: List<String>,
        width: Int,
        height: Int,
    ): Positionm {
        val faultSegments =
            input
                .map { string ->
                    string.split(',').map(String::toInt)
                }.map { (first, second) -> Positionm(second, first) }

        val end = Positionm(height, width)
        val segmentIndex =
            (0..faultSegments.size).binarySearchLowerBound { i ->
                val path = findPath(faultSegments, i, width, height)
                end !in path
            }
        return faultSegments[segmentIndex - 1]
    }
}
