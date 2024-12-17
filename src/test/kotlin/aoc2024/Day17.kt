package aoc2024

import aoc2024.Instruction.*
import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEqualTo
import jdk.internal.module.DefaultRoots.compute
import org.junit.jupiter.api.Test
import kotlin.math.pow
import kotlin.time.measureTime

class Computer(
    var registerA: Long,
    var registerB: Long,
    var registerC: Long,
    val program: List<Int>,
    var output: MutableList<Int>,
) {
    fun executeProgram() {
        var instructionPointer = 0
        while (instructionPointer < program.size) {
            val instruction = Instruction.entries[program[instructionPointer]]
            instructionPointer++
            val operand = program[instructionPointer]
            instructionPointer++
            when (instruction) {
                adv -> {
                    registerA = (registerA / 2.0.pow(operand.asCombo().toInt())).toLong()
                }

                bxl -> {
                    registerB = registerB xor operand.asLiteral()
                }

                bst -> {
                    registerB = operand.asCombo() % 8
                }

                jnz -> {
                    if (registerA != 0L) {
                        instructionPointer = operand.asLiteral().toInt()
                    }
                }

                bxc -> {
                    registerB = registerB xor registerC
                }

                out -> {
                    output.add((operand.asCombo() % 8).toInt())
                }

                bdv -> {
                    registerB = (registerA / 2.0.pow(operand.asCombo().toInt())).toLong()
                }

                cdv -> {
                    registerC = (registerA / 2.0.pow(operand.asCombo().toInt())).toLong()
                }
            }
        }
    }

    fun Int.asCombo(): Long =
        when (this) {
            0, 1, 2, 3 -> this.toLong()
            4 -> registerA
            5 -> registerB
            6 -> registerC
            else -> error("shouldn't happen, but was $this")
        }

    fun Int.asLiteral(): Long = this.toLong()
}

enum class Instruction {
    adv,
    bxl,
    bst,
    jnz,
    bxc,
    out,
    bdv,
    cdv,
}

class Day17 {
    @Test
    fun operationsTest() {
        Computer(
            0,
            0,
            9,
            listOf(2, 6),
            mutableListOf(),
        ).also { computer ->
            computer.executeProgram()
            assertThat(computer.registerB).isEqualTo(1)
        }

        Computer(
            10,
            0,
            0,
            listOf(5, 0, 5, 1, 5, 4),
            mutableListOf(),
        ).also { computer ->
            computer.executeProgram()
            assertThat(computer.output).containsExactly(0, 1, 2)
        }

        Computer(
            0,
            29,
            0,
            listOf(1, 7),
            mutableListOf(),
        ).also { computer ->
            computer.executeProgram()
            assertThat(computer.registerB).isEqualTo(26)
        }

        Computer(
            0,
            2024,
            43690,
            listOf(4, 0),
            mutableListOf(),
        ).also { computer ->
            computer.executeProgram()
            assertThat(computer.registerB).isEqualTo(44354)
        }

        Computer(
            2024,
            0,
            0,
            listOf(0, 1, 5, 4, 3, 0),
            mutableListOf(),
        ).also { computer ->
            computer.executeProgram()
            assertThat(computer.output).containsExactly(4, 2, 5, 6, 7, 7, 7, 7, 3, 1, 0)
            assertThat(computer.registerA).isEqualTo(0)
        }
    }

    @Test
    fun part1Example1() {
        val input =
            """
Register A: 729
Register B: 0
Register C: 0

Program: 0,1,5,4,3,0
            """.trimIndent()
                .lines()
        val result = part1(input)
        assertThat(result).isEqualTo("4,6,3,5,6,3,5,2,1,0")
    }

    @Test
    fun part1Input() {
        val lines = fileInput("Day17.txt").readLines()
        val duration =
            measureTime {
                val result = part1(lines)
                assertThat(result).isEqualTo("4,1,7,6,4,1,0,2,7")
            }
        println("part1 $duration")
    }

    fun part1(input: List<String>): String {
        val registerA = input[0].substringAfter("Register A: ").toLong()
        val registerB = input[1].substringAfter("Register B: ").toLong()
        val registerC = input[2].substringAfter("Register C: ").toLong()
        val program = input[4].substringAfter("Program: ")
        val instructions =
            program
                .split(',')
                .map(String::toInt)

        val computer =
            Computer(
                registerA,
                registerB,
                registerC,
                instructions,
                mutableListOf(),
            )
        computer.executeProgram()
        println(computer.output)
        return computer.output.joinToString(",")
    }

    @Test
    fun part2Example1() {
        val input =
            """

            """.trimIndent()
                .lines()
        val result = part2Sample(input)
        assertThat(result).isEqualTo(117440)
    }

    @Test
    fun part2Input() {
        val lines = fileInput("Day17.txt").readLines()
        val duration =
            measureTime {
                val result = part2(lines)
                assertThat(result).isEqualTo(117440)
            }
        println("part2 $duration")
    }

    fun part2Sample(input: List<String>): Long {
        val registerA =
            binarySearchOnRotatedArraysWithDuplicateElements(
                listOf(0, 3, 5, 4, 3, 0).reversed().joinToString("").toLong(),
                0,
                Int.MAX_VALUE.toLong(),
                -1,
            ) { registerA ->
                val computer =
                    Computer(
                        registerA,
                        0,
                        0,
                        listOf(0, 3, 5, 4, 3, 0),
                        mutableListOf(),
                    )
                computer.executeProgram()
                return@binarySearchOnRotatedArraysWithDuplicateElements computer.output
                    .reversed()
                    .joinToString("")
                    .toLong()
            }
        return registerA
    }

    fun part2(input: List<String>): Long {
        var registerA = 300_000L
        while (registerA < 600_000) {
            val message = compute(registerA).joinToString("")
            if (message.startsWith("24117")) {
                println("$registerA to $message")
            }
            registerA++
        }

//        val registerAValue =
//            binarySearchOnRotatedArraysWithDuplicateElements(
//                listOf(2, 4, 1, 1, 7, 5, 1, 5, 4, 0, 5, 5, 0, 3, 3, 0)
// //                    .reversed()
//                    .joinToString("")
//                    .toLong(8),
//                0,
//                Long.MAX_VALUE,
//                -1,
//            ) { registerA ->
//
//                val compute = compute(registerA)
// //                if (compute.size > 19) {
// //                    return@binarySearchOnRotatedArraysWithDuplicateElements Long.MAX_VALUE
// //                } else {
//                val toLong =
//                    compute
// //                        .reversed()
//                        .joinToString("")
//                        .toLong(8)
//                println(toLong)
//                return@binarySearchOnRotatedArraysWithDuplicateElements toLong
// //                }
//            }
        return 0
//        return registerAValue
    }

    fun compute(registerA: Long): List<Int> {
        val computer =
            Computer(
                registerA,
                0,
                0,
                // 16
                listOf(2, 4, 1, 1, 7, 5, 1, 5, 4, 0, 5, 5, 0, 3, 3, 0),
                mutableListOf(),
            )
        computer.executeProgram()
        return computer.output
    }

    @Test
    fun assertBinarySearch() {
        val input =
            listOf(
                5,
                5,
                6,
                6,
                6,
                7,
                7,
                7,
                8,
                8,
                8,
                9,
                9,
                0,
                0,
                0,
                0,
                1,
                1,
                2,
                3,
                3,
                3,
                4,
                4,
            )

        val find =
            binarySearchOnRotatedArraysWithDuplicateElements(0, 0, input.size.toLong() - 1, -1) {
                input[it.toInt()].toLong()
            }
        assertThat(find).isEqualTo(13)
    }

    fun binarySearchOnRotatedArraysWithDuplicateElements(
        key: Long,
        low: Long,
        high: Long,
        result: Long,
        arr: (current: Long) -> Long,
    ): Long {
        val mid = (low + high) / 2
        // key not present
        if (low > high) {
            return result
        }

        if (arr(mid) == key) {
            // we are not sure this is final result
            return binarySearchOnRotatedArraysWithDuplicateElements(key, low, mid - 1, mid, arr)
        }

        // if left half is sorted.
        if (arr(low) <= arr(mid)) {
            // key found

            // if key is present in left half.
            return if (arr(low) <= key && arr(mid) >= key) {
                binarySearchOnRotatedArraysWithDuplicateElements(key, low, mid - 1, result, arr)
            } else {
                // if key is not present in left half..search right half.
                binarySearchOnRotatedArraysWithDuplicateElements(key, mid + 1, high, result, arr)
            }
        } else { // if right half is sorted.
            // if key is present in right half.
            return if (arr(mid) <= key && arr(high) >= key) {
                binarySearchOnRotatedArraysWithDuplicateElements(key, mid + 1, high, result, arr)
            } else {
                // if key is not present in right half..search in left half.
                binarySearchOnRotatedArraysWithDuplicateElements(key, low, mid - 1, result, arr)
            }
        }
    }

//
//    else
//    end-if
//    end-if
//
//    end-function
}
