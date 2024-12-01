@file:OptIn(ExperimentalStdlibApi::class)

package aoc2023

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test
import kotlin.time.measureTime

class Day8 {
    @Test
    fun part1Example() {
        val input =
            $"""
            LLR
            
            AAA = (BBB, BBB)
            BBB = (AAA, ZZZ)
            ZZZ = (ZZZ, ZZZ)
            """.trimIndent()
                .lines()
        val result = part1(input)
        assertThat(result).isEqualTo(6)
    }

    @Test
    fun part1Input() {
        val lines = fileInput("Day8.txt").readLines()
        val duration = measureTime {
            val result = part1(lines)
            assertThat(result).isEqualTo(18673)
        }
        println("part1 $duration")
    }

    data class Node(val left: String, val right: String)

    fun part1(input: List<String>): Int {
        val directions = input.first()
            .chunked(1)
            .also(::println)

        val elements = input.drop(2)
            .associateBy({ str ->
                val (node, _, _) = str.split(" = (", ", ", ")")
                node
            }, { str ->
                val (_, left, right) = str.split(" = (", ", ", ")")
                Node(left, right)
            })
            .also(::println)

        var currentNode = "AAA"
        var operationCount = 0
        while (true) {
            if (currentNode == "ZZZ") {
                return operationCount
            }
            val node = elements[currentNode]!!
            when (directions[directions.circleIndex(operationCount)]) {
                "L" -> currentNode = node.left
                "R" -> currentNode = node.right
            }
            operationCount++
        }
    }

    private fun List<String>.circleIndex(index: Int): Int =
        if (index < 0) (index % size + size) % size
        else index % size

    @Test
    fun part2Example() {
        val input =
            $"""
            LR
            
            11A = (11B, XXX)
            11B = (XXX, 11Z)
            11Z = (11B, XXX)
            22A = (22B, XXX)
            22B = (22C, 22C)
            22C = (22Z, 22Z)
            22Z = (22B, 22B)
            XXX = (XXX, XXX)
            """.trimIndent()
                .lines()
        val result = part2(input)
        assertThat(result).isEqualTo(6)
    }

    @Test
    fun part2Input() {
        val lines = fileInput("Day8.txt").readLines()
        val duration = measureTime {
            val result = part2(lines)
            assertThat(result).isEqualTo(17972669116327)
        }
        println("part2 $duration")
    }

    data class Node2(val position: Int, val code: String, val left: String, val right: String)

    fun part2(input: List<String>): Long {
        val directions = input.first()
            .chunked(1)
            .map { str ->
                return@map when (str) {
                    "L" -> 0
                    "R" -> 1
                    else -> error("Shouldn't happen, but was $str")
                }
            }.toIntArray()


        val elements = mutableMapOf<String, Node2>()
        input.drop(2)
            .forEachIndexed { index, str ->
                val (node, left, right) = str.split(" = (", ", ", ")")
                elements.put(node, Node2(index, node, left, right))
            }
        println("Elements size: ${elements.size}")
        println("Starting elements:")
        var startingElements =
            elements.filterKeys { str -> str.endsWith('A') }
                .values
                .map { node2 -> node2.position }
                .toIntArray()
                .also { println(it.joinToString()) }

        println("End elements:")
        var endElements =
            elements.filterKeys { str -> str.endsWith('Z') }
                .values
                .map { node2 -> node2.position }
                .toIntArray()
                .also { println(it.joinToString()) }

        val elementsIndex = Array(2) { IntArray(elements.size) { -1 } }
        elements.forEach { t, u ->
            val position = u.position
            val leftPosition = elements[u.left]!!.position
            elementsIndex[0][position] = leftPosition
            val rightPosition = elements[u.right]!!.position
            elementsIndex[1][position] = rightPosition

//            println("$position ${u.code} ${u.left}-${leftPosition} ${u.right}-$rightPosition")
        }
        println("Elements index L size: ${elementsIndex[0].size}")
        println("Elements index R size: ${elementsIndex[1].size}")

        println("Verify all elements are searchable individually")

        val shortestPaths = (0..<startingElements.size).map { t ->
            val count = findShortestElement(
                IntArray(1) { startingElements[t] },
                endElements,
                elementsIndex,
                directions
            )
            println("Found ${startingElements[t]} to ${count.first} for ${count.second}")
            return@map count.second
        }.also(::println)

        // find minimum number by which all solutions can divide
        val min = shortestPaths.min()
        var current = min.toLong()
        while (true) {
            if (shortestPaths.all { current % it == 0L }) {
                return current
            }
            current += min
        }
    }

    fun findShortestElement(
        startElements: IntArray,
        endElements: IntArray,
        elementsIndex: Array<IntArray>,
        directions: IntArray
    ): Pair<Int, Int> {
        val currentElements = startElements
        var currentDirection = 0
        var operationCount = 0
        while (true) {
            if (endElements.contains(currentElements[0])) {
                return currentElements[0] to operationCount
            }
            if (currentDirection == directions.size) {
                currentDirection = 0 // reset and loop directions in circle
            }

            for (i in 0..<currentElements.size) {
                val curr = currentElements[i]
                //
                currentElements[i] = elementsIndex[directions[currentDirection]][curr]
            }

            operationCount++
            currentDirection++
        }
    }
}
