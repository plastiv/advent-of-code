package aoc2024

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test
import kotlin.time.measureTime

class Day6 {
    @Test
    fun part1Example() {
        val input =
            $"""
            ....#.....
            .........#
            ..........
            ..#.......
            .......#..
            ..........
            .#..^.....
            ........#.
            #.........
            ......#...
            """.trimIndent()
                .lines()
        val result = part1(input)
        assertThat(result).isEqualTo(41)
    }

    @Test
    fun part1Input() {
        val lines = fileInput("Day6.txt").readLines()
        val duration = measureTime {
            val result = part1(lines)
            assertThat(result).isEqualTo(5563)
        }
        println("part1 $duration")
    }

    fun part1(input: List<String>): Int {
        val map = input.toCharGrid()
        val start = map
            .elements
            .firstNotNullOf { entry -> if (entry.value == '^') Pair(entry.key, entry.value) else null }

        start
            .also(::println)

        return getGuardPositions(map, start.first, start.second, 0).size
    }

    // north = 0
    // west = 1
    // south = 2
    // east = 3

    @Test
    fun part2Example() {
        val input =
            $"""
            ....#.....
            .........#
            ..........
            ..#.......
            .......#..
            ..........
            .#..^.....
            ........#.
            #.........
            ......#...
            """.trimIndent()
                .lines()
        val result = part2(input)
        assertThat(result).isEqualTo(6)
    }

    @Test
    fun part2Input() {
        val lines = fileInput("Day6.txt").readLines()
        val duration = measureTime {
            val result = part2(lines)
            assertThat(result).isEqualTo(1976)
        }
        println("part2 $duration")
    }

    fun part2(input: List<String>): Int {
        val map = input.toCharGrid()
        val start = map
            .elements
            .firstNotNullOf { entry -> if (entry.value == '^') Pair(entry.key, entry.value) else null }

        start
            .also(::println)

        val guardPositions = getGuardPositions(map, start.first, start.second, 0)
        val guardPath = guardPositions.map { entry ->
            isLoop(map, start.first, start.second, 0, entry.key)
        }
        println("Lookup size: ${guardPath.size}")
        return guardPath.count { bool -> bool == true }
    }

    fun getGuardPositions(
        map: Grid<Char>,
        startPosition: Positionm,
        startChar: Char,
        startDirection: Int
    ): Map<Positionm, Int> {
        val visited = mutableMapOf<Positionm, Int>()
        var currentPosition = startPosition
        var currentChar: Char? = startChar
        var currentDirection = startDirection

        while (currentChar != null) {// outside of visible map
            // if peek next char is obsticle
            val peekNextPosition = when (currentDirection) {
                0 -> currentPosition.north()
                1 -> currentPosition.west()
                2 -> currentPosition.south()
                3 -> currentPosition.east()
                else -> error("Shouldn't happen but was $currentDirection")
            }
            val peekNextChar = map.elements[peekNextPosition]
            if (peekNextChar == '#') {
                // change direction
                currentDirection = (currentDirection + 1) % 4 // loop around
                continue
            }
            // move step further
            currentPosition = peekNextPosition
            currentChar = map.elements[currentPosition]
            if (currentChar != null) {
                visited.put(currentPosition, currentDirection)
            }
        }
        return visited.toMap()
    }

    fun isLoop(
        map: Grid<Char>,
        startPosition: Positionm,
        startChar: Char,
        startDirection: Int,
        obst: Positionm
    ): Boolean {
        val visited = mutableMapOf<Positionm, Int>()
        var currentPosition = startPosition
        var currentChar: Char? = startChar
        var currentDirection = startDirection
        val visitedCount = visited.getOrDefault(currentPosition, 0)
        visited.put(currentPosition, visitedCount + 1)

        while (currentChar != null) {// outside of visible map
            // if peek next char is obsticle
            val peekNextPosition = when (currentDirection) {
                0 -> currentPosition.north()
                1 -> currentPosition.west()
                2 -> currentPosition.south()
                3 -> currentPosition.east()
                else -> error("Shouldn't happen but was $currentDirection")
            }
            val peekNextChar = map.elements[peekNextPosition]
            if (peekNextChar == '#' || peekNextPosition == obst) {
                // change direction
                currentDirection = (currentDirection + 1) % 4 // loop around
                continue
            }
            // move step further
            currentPosition = peekNextPosition
            currentChar = map.elements[currentPosition]

            val visitedCount = visited.getOrDefault(currentPosition, 0)
            if (visitedCount > 3) { // white, grey, black
                return true
            }
            if (currentChar != null) {
                visited.put(currentPosition, visitedCount + 1)
            }
        }
        return false
    }
}
