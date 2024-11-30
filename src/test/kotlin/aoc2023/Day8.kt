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
}
