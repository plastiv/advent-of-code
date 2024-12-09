package aoc2024

import assertk.assertThat
import assertk.assertions.isEqualTo
import utils.swap
import kotlin.test.Test
import kotlin.time.measureTime

const val EMPTY = -1

class Day9 {
    @Test
    fun part1Example() {
        val input =
            $"""
            2333133121414131402
            """.trimIndent()
                .lines()
        val result = part1(input)
        assertThat(result).isEqualTo(1928)
    }

    @Test
    fun part1Input() {
        val lines = fileInput("Day9.txt").readLines()
        val duration = measureTime {
            val result = part1(lines)
            assertThat(result).isEqualTo(6430446922192)
        }
        println("part1 $duration")
    }

    fun part1(input: List<String>): Long {
        val blocks = mutableListOf<Int>()

        input.first()
            .map(Char::digitToInt)
            .forEachIndexed { index, int ->
                if (index % 2 == 0) {
                    // file block
                    repeat(int) {
                        // index / 2 == fileId
                        blocks += index / 2
                    }
                } else {
                    repeat(int) {
                        blocks += EMPTY
                    }
                }
            }

        var firstEmptyIndex = blocks.indexOfFirst { i -> i == EMPTY }
        var lastNonEmptyIndex = blocks.indexOfLast { i -> i != EMPTY }
        while (firstEmptyIndex <= lastNonEmptyIndex) {
            // take last non-empty, replace with empty
            blocks.swap(firstEmptyIndex, lastNonEmptyIndex)
            // find first empty, replace with taken
            firstEmptyIndex = blocks.indexOfFirst { i -> i == EMPTY }
            lastNonEmptyIndex = blocks.indexOfLast { i -> i != EMPTY }
        }

        return blocks.mapIndexed { index, i ->
            if (i == EMPTY) {
                0L
            } else {
                (index * i).toLong()
            }
        }.sum()
    }

    @Test
    fun part2Example() {
        val input =
            $"""
            2333133121414131402
            """.trimIndent()
                .lines()
        val result = part2(input)
        assertThat(result).isEqualTo(2858)
    }

    @Test
    fun part2Input() {
        val lines = fileInput("Day9.txt").readLines()
        val duration = measureTime {
            val result = part2(lines)
            assertThat(result).isEqualTo(6460170593016)
        }
        println("part2 $duration")
    }

    sealed interface Block

    data class FileBlock(val size: Int, val fileId: Int) : Block

    data class EmptyBlock(val size: Int) : Block

    fun part2(input: List<String>): Long {
        val blocks = mutableListOf<Block>()

        var fileId = 0
        input.first()
            .map(Char::digitToInt)
            .forEachIndexed { index, int ->
                if (index % 2 == 0) {
                    // file block
                    blocks += FileBlock(int, fileId)

                    fileId++
                } else {
                    // empty block
                    blocks += EmptyBlock(int)
                }
            }

        // from maximum file ID to 0
        // find element position with current maximum file
        // search for empty block which can fit the element
        // must be before block position
        // if not available leave it be
        // else take that block (remove)
        // and put into empty space
        fileId-- // index was for the file one after last one
        (0..fileId).reversed().forEach { currentFileId ->
            val fileBlockPosition = blocks.indexOfLast { block ->
                block is FileBlock && block.fileId == currentFileId
            }
            val fileBlock = blocks[fileBlockPosition] as FileBlock

            val emptyBlockPosition = blocks.indexOfFirst { block ->
                block is EmptyBlock && fileBlock.size <= block.size
            }
            // can be moved to the left?
            if (emptyBlockPosition < fileBlockPosition && emptyBlockPosition != -1) {
                val emptyBlock = blocks[emptyBlockPosition] as EmptyBlock
                val leftoverEmptySpace = emptyBlock.size - fileBlock.size
                blocks[fileBlockPosition] = EmptyBlock(fileBlock.size)

                blocks.removeAt(emptyBlockPosition)
                blocks.add(emptyBlockPosition, fileBlock)
                if (leftoverEmptySpace > 0) {
                    blocks.add(emptyBlockPosition + 1, EmptyBlock(leftoverEmptySpace))
                }
            }
        }

        return blocks.fold(0 to 0L) { (index, sum), block ->
            return@fold when (block) {
                is EmptyBlock -> Pair(index + block.size, sum)
                is FileBlock -> {
                    var tsum = sum
                    var tindex = index
                    repeat(block.size) { _ ->
                        tsum += (tindex * block.fileId).toLong()
                        tindex++
                    }
                    Pair(tindex, tsum)
                }
            }
        }.second
    }
}
