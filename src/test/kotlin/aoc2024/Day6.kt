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
            assertThat(result).isEqualTo(5564)
        }
        println("part1 $duration")
    }

    fun part1(input: List<String>): Int {
        val map = input.toCharGrid()
        val start = map
            .elements
            .firstNotNullOf { entry -> if (entry.value == '^') entry.key else null }

        start
            .also(::println)

        return getGuardPositions(map, start, 0).size
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
            .firstNotNullOf { entry -> if (entry.value == '^') entry.key else null }

        return getGuardPositions(map, start, 0)
            .drop(1) // exclude starting point from obstacle candidates
            .map { (position, direction) ->
                // avoid restarting guard path from the start as optimization
                val prevPosition = position.stepBack(direction)
                isLoop(map, prevPosition, direction, position)
            }.count { bool -> bool == true }
    }

    fun Positionm.move(direction: Int) = when (direction) {
        0 -> this.north()
        1 -> this.west()
        2 -> this.south()
        3 -> this.east()
        else -> error("Shouldn't happen but was $direction")
    }

    fun Positionm.stepBack(facingDirection: Int) = when (facingDirection) {
        0 -> this.south()
        1 -> this.east()
        2 -> this.north()
        3 -> this.west()
        else -> error("Shouldn't happen but was $facingDirection")
    }

    fun getGuardPositions(
        map: Grid<Char>,
        startPosition: Positionm,
        startDirection: Int
    ): List<Pair<Positionm, Int>> {
        val visited = mutableListOf<Pair<Positionm, Int>>()
        var currentPosition = startPosition
        var currentChar: Char? = '^'
        var currentDirection = startDirection

        visited.add(Pair(currentPosition, currentDirection))

        while (currentChar != null) {// outside of visible map
            // if peek next char is obstacle
            val peekNextPosition = currentPosition.move(currentDirection)
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
                visited.add(Pair(currentPosition, currentDirection))
            }
        }
        return visited.distinctBy { (position, _) -> position }
    }

    fun isLoop(
        map: Grid<Char>,
        startPosition: Positionm,
        startDirection: Int,
        obst: Positionm
    ): Boolean {
        val visited = mutableListOf<Pair<Positionm, Int>>()
        var currentPosition = startPosition
        var currentChar: Char? = '^'
        var currentDirection = startDirection

        while (currentChar != null) {// outside of visible map
            // if peek next char is obstacle
            val peekNextPosition = currentPosition.move(currentDirection)
            val peekNextChar = map.elements[peekNextPosition]
            if (peekNextChar == '#' || peekNextPosition == obst) {
                // change direction
                val visitedTurnRight = Pair(currentPosition, currentDirection)
                if (visitedTurnRight in visited) {
                    // already visited this location, found a loop
                    return true
                } else {
                    visited.add(visitedTurnRight)
                }
                currentDirection = (currentDirection + 1) % 4 // loop around
                continue
            }
            // move step further
            currentPosition = peekNextPosition
            currentChar = map.elements[currentPosition]
        }
        return false
    }
}
