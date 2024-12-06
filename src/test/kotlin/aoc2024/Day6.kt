package aoc2024

import assertk.assertThat
import assertk.assertions.isEqualTo
import java.util.Map.entry
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

    val Char.isGuard: Boolean
        get() = this == '^'

    val Char.isObsticle: Boolean
        get() = this == '#'

    fun part1(input: List<String>): Int {
        val map = input.toCharGrid()
        val start = map
            .elements
            .filterValues { ch -> ch.isGuard }
            .toList()
            .first()

        start
            .also(::println)

        val visited = mutableMapOf<Positionm, Boolean>()
        var currentPosition = start.first
        var currentChar: Char? = start.second
        var currentDirection = 1

        while (currentChar != null) {// outside of visible map
            // if peek next char is obsticle
            val peekNextPosition = when (currentDirection) {
                1 -> currentPosition.north()
                2 -> currentPosition.west()
                3 -> currentPosition.south()
                4 -> currentPosition.east()
                else -> error("Shouldn't happen but was $currentDirection")
            }
            val peekNextChar = map.elements[peekNextPosition]
            if (peekNextChar == '#') {
                // change direction
                currentDirection++
                if (currentDirection == 5) {
                    currentDirection = 1
                }
            }
            // move step further
            currentPosition = when (currentDirection) {
                1 -> currentPosition.north()
                2 -> currentPosition.west()
                3 -> currentPosition.south()
                4 -> currentPosition.east()
                else -> error("Shouldn't happen but was $currentDirection")
            }
            currentChar = map.elements[currentPosition]
            visited.put(currentPosition, true)
        }
        return visited.size - 1
    }

    // north = 1
    // west = 2
    // south = 3
    // east = 4

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
            // 5474 too high
            // 1934 too low
            //
            assertThat(result).isEqualTo(1976)
        }
        println("part2 $duration")
    }

    fun part2(input: List<String>): Int {
        val map = input.toCharGrid()
        val start = map
            .elements
            .filterValues { ch -> ch.isGuard }
            .toList()
            .first()

        start
            .also(::println)

        val guardPositions = getGuardPositions(map, start.first, start.second, 1)
//        printVisited(map, guardPositions)

//        val candidate = Positionm(27, 24)
//        val newMap = addObsticle(map, candidate)
//        printVisited(map, guardPositions, candidate)
//        isLoop2(newMap, start.first, start.second, 1, candidate)

//        return 0

        return map.elements.map { entry ->
            if (entry.value != '^' && entry.value != '#') {
                val newMap = addObsticle(map, entry.key)
//                println("Check variant ${entry.key} ${entry.value}")
                isLoop2(newMap, start.first, start.second, 1, entry.key)
            } else {
                false
            }
        }.count { bool -> bool == true }
    }

    fun addObsticle(map: Grid<Char>, positionm: Positionm): Grid<Char> {
        val toMutableMap = map.elements.toMutableMap()
        toMutableMap.put(positionm, '#')
        return Grid(toMutableMap.toMap(), map.lines, map.rowSize, map.colSize)
    }

    fun printVisited(map: Grid<Char>, visited: Map<Positionm, Boolean>, obst: Positionm? = null) {
        val toMutableMap = map.elements.toMutableMap()
        visited.keys.forEach { t ->
            toMutableMap.put(t, 'X')
        }
        if (obst != null

        ) {
            toMutableMap.put(obst, '0')
        }
        var myArray = Array<Array<Char>>(map.rowSize, { Array<Char>(map.colSize) { '0' } })
        toMutableMap.forEach { t, u ->
            myArray[t.row][t.col] = u
        }
        myArray.forEach { chars ->
            println(chars.joinToString(""))
        }
    }

    fun getGuardPositions(
        map: Grid<Char>,
        startPosition: Positionm,
        startChar: Char,
        startDirection: Int
    ): Map<Positionm, Boolean> {
        val visited = mutableMapOf<Positionm, Boolean>()
        var currentPosition = startPosition
        var currentChar: Char? = startChar
        var currentDirection = startDirection

        while (currentChar != null) {// outside of visible map
            // if peek next char is obsticle
            val peekNextPosition = when (currentDirection) {
                1 -> currentPosition.north()
                2 -> currentPosition.west()
                3 -> currentPosition.south()
                4 -> currentPosition.east()
                else -> error("Shouldn't happen but was $currentDirection")
            }
            val peekNextChar = map.elements[peekNextPosition]
            if (peekNextChar == '#') {
                // change direction
                currentDirection++
                if (currentDirection == 5) {
                    currentDirection = 1
                }
            }
            // move step further
            currentPosition = when (currentDirection) {
                1 -> currentPosition.north()
                2 -> currentPosition.west()
                3 -> currentPosition.south()
                4 -> currentPosition.east()
                else -> error("Shouldn't happen but was $currentDirection")
            }
            currentChar = map.elements[currentPosition]
            if (currentChar != null) {
                visited.put(currentPosition, true)
            }
        }
        return visited.toMap()
    }

    fun isLoop2(
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
        var operationCount = 0
        while (currentChar != null) {// outside of visible map
            operationCount++
            // if peek next char is obsticle
            val peekNextPosition = when (currentDirection) {
                1 -> currentPosition.north()
                2 -> currentPosition.west()
                3 -> currentPosition.south()
                4 -> currentPosition.east()
                else -> error("Shouldn't happen but was $currentDirection")
            }

            visited.put(currentPosition, currentDirection)

            val peekNextChar = map.elements[peekNextPosition]
            if (peekNextChar == '#') {
                // change direction
                currentDirection++
                if (currentDirection == 5) {
                    currentDirection = 1
                }
            }
            // move step further
            currentPosition = when (currentDirection) {
                1 -> currentPosition.north()
                2 -> currentPosition.west()
                3 -> currentPosition.south()
                4 -> currentPosition.east()
                else -> error("Shouldn't happen but was $currentDirection")
            }
            currentChar = map.elements[currentPosition]

            if (operationCount > 20000) {
//                printVisited(map, visited.mapValues { entry -> true })
                return true
            }
        }
        return false
    }
}
