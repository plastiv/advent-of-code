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

    fun Positionm.neighbor(direction: Direction): Positionm =
        when (direction) {
            Direction.N -> this.north()
            Direction.E -> this.east()
            Direction.S -> this.south()
            Direction.W -> this.west()
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

        val visited = mutableSetOf<Pair<Positionm, Direction>>()
        val queue = PriorityQueue<State>(compareBy { it.distance })
        queue += State(start, Direction.E, 0, null)
        var bestDistance = Int.MAX_VALUE
        while (queue.isNotEmpty()) {
            val state =
                queue.remove().also { visited += it.position to it.direction }
            if (state.position == end) {
                if (state.distance <= bestDistance) {
                    bestDistance = state.distance
                } else {
                    break
                }
            }
            queue +=
                buildList {
                    val left = state.direction.rotateLeft()
                    if (grid.elements[state.position.move(left)] != '#') {
                        add(State(state.position, left, state.distance + 1000, state))
                    }
                    val right = state.direction.rotateRight()
                    if (grid.elements[state.position.move(right)] != '#') {
                        add(State(state.position, right, state.distance + 1000, state))
                    }
                    if (grid.elements[state.position.move(state.direction)] != '#') {
                        add(State(state.position.move(state.direction), state.direction, state.distance + 1, state))
                    }
                }.filter { (it.position to it.direction) !in visited }
        }

        return bestDistance
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
                assertThat(result).isEqualTo(451)
                // 464 is too high
                // 463 other person input
                // 462 is too high
                // 482 is not the right answer
            }
        println("part2 $duration")
    }

    data class State(
        val position: Positionm,
        val direction: Direction,
        val distance: Int,
        val previous: State?,
    )

    fun Direction.rotateLeft(): Direction =
        when (this) {
            Direction.N -> Direction.W
            Direction.E -> Direction.N
            Direction.S -> Direction.E
            Direction.W -> Direction.S
        }

    fun Direction.rotateRight(): Direction =
        when (this) {
            Direction.N -> Direction.E
            Direction.E -> Direction.S
            Direction.S -> Direction.W
            Direction.W -> Direction.N
        }

    fun Positionm.move(direction: Direction): Positionm = this.neighbor(direction)

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

        val visited = mutableSetOf<Pair<Positionm, Direction>>()
        val queue = PriorityQueue<State>(compareBy { it.distance })
        queue += State(start, Direction.E, 0, null)
        val bestPoints = mutableSetOf(end)
        var bestDistance = Int.MAX_VALUE
        while (queue.isNotEmpty()) {
            val state =
                queue.remove().also { visited += it.position to it.direction }
            if (state.position == end) {
                if (state.distance <= bestDistance) {
                    bestDistance = state.distance
                    var current = state
                    do {
                        bestPoints += current.position
                        current = current.previous
                    } while (current != null)
                } else {
                    break
                }
            }
            queue +=
                buildList {
                    val left = state.direction.rotateLeft()
                    if (grid.elements[state.position.move(left)] != '#') {
                        add(State(state.position, left, state.distance + 1000, state))
                    }
                    val right = state.direction.rotateRight()
                    if (grid.elements[state.position.move(right)] != '#') {
                        add(State(state.position, right, state.distance + 1000, state))
                    }
                    if (grid.elements[state.position.move(state.direction)] != '#') {
                        add(State(state.position.move(state.direction), state.direction, state.distance + 1, state))
                    }
                }.filter { (it.position to it.direction) !in visited }
        }

        return bestPoints.size
    }
}
