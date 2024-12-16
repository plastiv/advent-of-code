package aoc2024

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test
import utils.swap
import kotlin.time.measureTime

class Day15 {
    @Test
    fun part1Example() {
        val input =
            """
            ##########
            #..O..O.O#
            #......O.#
            #.OO..O.O#
            #..O@..O.#
            #O#..O...#
            #O..O..O.#
            #.OO.O.OO#
            #....O...#
            ##########

            <vv>^<v^>v>^vv^v>v<>v^v<v<^vv<<<^><<><>>v<vvv<>^v^>^<<<><<v<<<v^vv^v>^
            vvv<<^>^v^^><<>>><>^<<><^vv^^<>vvv<>><^^v>^>vv<>v<<<<v<^v>^<^^>>>^<v<v
            ><>vv>v^v^<>><>>>><^^>vv>v<^^^>>v^v^<^^>v^^>v^<^v>v<>>v^v^<v>v^^<^^vv<
            <<v<^>>^^^^>>>v^<>vvv^><v<<<>^^^vv^<vvv>^>v<^^^^v<>^>vvvv><>>v^<<^^^^^
            ^><^><>>><>^^<<^^v>>><^<v>^<vv>>v>>>^v><>^v><<<<v>>v<v<v>vvv>^<><<>^><
            ^>><>^v<><^vvv<^^<><v<<<<<><^v<<<><<<^^<v<^^^><^>>^<v^><<<^>>^v<v^v<v^
            >^>>^v>vv>^<<^v<>><<><<v<<v><>v<^vv<<<>^^v^>^^>>><<^v>>v^v><^^>>^<>vv^
            <><^^>^^^<><vvvvv^v<v<<>^v<v>v<<^><<><<><<<^^<<<^<<>><<><^^^>^^<>^>v<>
            ^^>vv<^v^v<vv>^<><v<^v>^^^>>>^^vvv^>vvv<>>>^<^>>>>>^<<^v>^vvv<>^<><<v>
            v^^>>><<^^<>>^v^<v^vv<>v^<<>^<^v^v><^<<<><<^<v><v<>vv>>v><v^<vv<>v^<<^
            """.trimIndent()
                .lines()
        val result = part1(input)
        assertThat(result).isEqualTo(0)
    }

    fun String.collapse(): String {
        require(this.isNotEmpty())
        require(this.first() != '.')
//        require(this.last() == '#')
        val chars = this.toList()
        val indexOfFirstDot = chars.indexOfFirst { it == '.' }
        if (indexOfFirstDot != -1) { // found
            return chars.swap(0, indexOfFirstDot).joinToString("")
        }
        return this
    }

    @Test
    fun part1Input() {
        val lines = fileInput("Day15.txt").readLines()
        val duration =
            measureTime {
                val result = part1(lines)
                assertThat(result).isEqualTo(0)
            }
        println("part1 $duration")
    }

    fun Positionm.next(direction: Char): Positionm =
        when (direction) {
            '>' -> this.east()
            '<' -> this.west()
            '^' -> this.north()
            'v' -> this.south()
            else -> error("shouldn't happen but was $direction")
        }

    fun Grid<Char>.takeUntilWall(
        position: Positionm,
        direction: Char,
    ): List<Pair<Positionm, Char>> {
        var current = position
        return buildList {
            var value = elements[current]!!
            while (value != '#') {
                add(current to value)
                current = current.next(direction)
                value = elements[current]!!
            }
        }
    }

    fun part1(input: List<String>): Int {
//        assertThat(".0.0.#".collapse()).isEqualTo("...00#")
        assertThat("0.0.#".collapse()).isEqualTo(".00.#")
        val grid =
            input
                .takeWhile { it.isNotEmpty() }
                .toCharGrid()
//                .also(::println)

        val directions =
            input
                .takeLastWhile { it.isNotEmpty() }
                .joinToString("")
//                .also(::println)

        val startingPos = grid.elements.firstNotNullOf { entry -> if (entry.value == '@') entry.key else null }

//        grid.elements.put(startingPos, '.')
        var current = startingPos
        directions.forEach { direction ->
            println("------------------")
            println("Current $current next $direction")
            println("------------------")
            println(grid)
            val candidate = current.next(direction)
            val candidateValue = grid.elements[candidate]
            when (candidateValue) {
                '.' -> {
                    grid.elements.put(current, '.')
                    current = candidate
                    grid.elements.put(current, '@')
                }

                'O' -> {
                    // attempt to move
                    // @ 0 0 . 0 . 0 . . #

                    // find elements in direction until # as string
                    val pairs = grid.takeUntilWall(candidate, direction)
                    val positions = pairs.map(Pair<Positionm, Char>::first)
                    val chars = pairs.map(Pair<Positionm, Char>::second)
                    val original = chars.joinToString("")
                    val collapsed = original.collapse()
                    if (collapsed != original) { // always true
                        // optimization check
                        positions.zip(collapsed.toList()).forEach { (position, char) ->
                            grid.elements.put(position, char)
                        }
                        //
                        if (collapsed.first() == '.') {
                            grid.elements.put(current, '.')
                            current = candidate
                            grid.elements.put(current, '@')
                        }
                    }
                    // do nothing when next element is 0 and can't be moved
                }

                '#' -> {
                    // do nothing
                }

                else -> error("Shouldn't happen but was $candidateValue")
            }
        }
        return grid.elements
            .filter { entry -> entry.value == 'O' }
            .keys
            .sumOf { (r, c) ->
                r * 100 + c
            }
    }

    @Test
    fun part2Example1() {
        val input =
            """
#######
#...#.#
#.....#
#..OO@#
#..O..#
#.....#
#######

<vv<<^^<<^^
            """.trimIndent()
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

    fun String.cycle(): String {
        require(this.isNotEmpty())
        require(this.first() != '.')
        val chars = this.toList()
        val indexOfFirstDot = chars.indexOfFirst { it == '.' }
        if (indexOfFirstDot != -1) { // found
            val toMutableList = chars.toMutableList()
            toMutableList.removeAt(indexOfFirstDot)
            toMutableList.addFirst('.')
            return toMutableList.joinToString("")
        }
        return this
    }

    fun Grid<Char>.swap(
        position1: Positionm,
        position2: Positionm,
    ) {
        val ch1 = elements[position1]!!
        val ch2 = elements[position2]!!
        elements[position2] = ch1
        elements[position1] = ch2
    }

    fun Grid<Char>.moveUp2(position: Positionm) {
        // []  []
        // *    *
        if (canMoveUp(position)) {
            when (elements[position.north()]) {
                '[' -> {
                    // box above
                    // attempt to move
                    moveUp2(position.north() to position.east().north())
                }

                ']' -> {
                    moveUp2(position.north() to position.west().north())
                }

                '.' -> {
                    swap(position, position.north())
                }

                '#' -> {
                    // can't move up
                }

                else -> error("shouldn't happen, but was ${elements[position.north()]}")
            }
        }
    }

    fun Grid<Char>.moveUp2(box: Pair<Positionm, Positionm>) {
        if (canMoveUp(box.first) && canMoveUp(box.second)) {
            val char = Pair(elements[box.first], elements[box.second])
            when (char) {
                '[' to ']' -> {
                    // if can move up
                    moveUp2(box.first.north() to box.second.north())
                    swap(box.first, box.first.north())
                    swap(box.second, box.second.north())
                }

                '.' to '[' -> {
                    // TODO: if can move up
                    moveUp2(box.second.north() to box.second.east().north())
                    swap(box.first, box.first.north())
                    swap(box.second, box.second.north())
                }

                ']' to '.' -> {
                    // TODO: if can move up
                    moveUp2(box.first.north() to box.first.west().north())
                    swap(box.first, box.first.north())
                    swap(box.second, box.second.north())
                }

                ']' to '[' -> {
                    // TODO: if can move up
                    moveUp2(box.first.north() to box.first.west().north())
                    moveUp2(box.second.north() to box.second.east().north())
                    swap(box.first, box.first.north())
                    swap(box.second, box.second.north())
                }

                '.' to '.' -> {
                    swap(box.first, box.first.north())
                    swap(box.second, box.second.north())
                }

                '#' to '#' -> {
                    // can't move up
                }

                else -> error("shouldn't happen, but was $char")
            }
        }
    }

    fun Grid<Char>.canMoveUp3(position: Positionm): List<Positionm> =
        when (elements[position.north()]) {
            '[' ->
                buildList {
                    if (canMoveUp(position.north()) && canMoveUp(position.east().north())) {
                        add(position)
                        addAll(canMoveUp3(position.north()))
                        addAll(canMoveUp3(position.east().north()))
                    }
                }

            ']' ->
                buildList {
                    if (canMoveUp(position.north()) && canMoveUp(position.west().north())) {
                        add(position)
                        addAll(canMoveUp3(position.north()))
                        addAll(canMoveUp3(position.west().north()))
                    }
                }

            '.' -> listOf(position)
            '#' -> emptyList()
            else -> error("shouldn't happen, but was ${elements[position.north()]}")
        }

    fun Grid<Char>.canMoveUp(position: Positionm): Boolean =
        when (elements[position.north()]) {
            '[' -> canMoveUp(position.north()) && canMoveUp(position.east().north())
            ']' -> canMoveUp(position.north()) && canMoveUp(position.west().north())
            '.' -> true
            '#' -> false
            else -> error("shouldn't happen, but was ${elements[position.north()]}")
        }

    fun Grid<Char>.moveUp(positions: List<Positionm>) {
        positions.map { positionm ->
        }
        positions.forEach { p ->
            val prev = elements[p]
            elements[p.north()]
        }
    }

    fun part2(input: List<String>): Int {
        assertThat("[][].[].#".cycle()).isEqualTo(".[][][].#")
        val newInput =
            input
                .takeWhile { it.isNotEmpty() }
                .map { string ->
                    string
                        .map { ch ->
                            when (ch) {
                                '@' -> {
                                    "$ch."
                                }

                                'O' -> "[]"
                                else -> {
                                    "$ch$ch"
                                }
                            }
                        }.joinToString("")
                }
        val grid =
            newInput
                .toCharGrid()

        val directions =
            input
                .takeLastWhile { it.isNotEmpty() }
                .joinToString("")

        val startingPos = grid.elements.firstNotNullOf { entry -> if (entry.value == '@') entry.key else null }

        var current = startingPos
        directions.forEach { direction ->
            println("------------------")
            println("Current $current next $direction")
            println("------------------")
            println(grid)
            val candidate = current.next(direction)
            val candidateValue = grid.elements[candidate]
            when (candidateValue) {
                '.' -> {
                    grid.elements.put(current, '.')
                    current = candidate
                    grid.elements.put(current, '@')
                }

                '[', ']' -> {
                    // attempt to move
                    // @ 0 0 . 0 . 0 . . #
                    when (direction) {
                        '<', '>' -> {
                            // find elements in direction until # as string
                            val pairs = grid.takeUntilWall(candidate, direction)
                            val positions = pairs.map(Pair<Positionm, Char>::first)
                            val chars = pairs.map(Pair<Positionm, Char>::second)
                            val original = chars.joinToString("")
                            val collapsed = original.cycle()
                            if (collapsed != original) { // always true
                                // optimization check
                                positions.zip(collapsed.toList()).forEach { (position, char) ->
                                    grid.elements.put(position, char)
                                }
                                //
                                if (collapsed.first() == '.') {
                                    grid.elements.put(current, '.')
                                    current = candidate
                                    grid.elements.put(current, '@')
                                }
                            }
                        }

                        '^', 'v' -> {
                            val toMoveUps = grid.canMoveUp3(current)
                            current = candidate
                        }

                        else -> error("shouldn't happen, but was $direction")
                    }
                    // do nothing when next element is 0 and can't be moved
                }

                '#' -> {
                    // do nothing
                }

                else -> error("Shouldn't happen but was $candidateValue")
            }
        }
        return grid.elements
            .filter { entry -> entry.value == 'O' }
            .keys
            .sumOf { (r, c) ->
                r * 100 + c
            }
    }
}
