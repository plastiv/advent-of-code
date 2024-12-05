package aoc2024

import assertk.assertThat
import assertk.assertions.isEqualTo
import jdk.javadoc.internal.doclets.formats.html.markup.HtmlStyle.index
import java.time.temporal.TemporalAdjusters.previous
import kotlin.test.Test
import kotlin.time.measureTime


class Day5 {
    @Test
    fun part1Example() {
        val input =
            $"""
47|53
97|13
97|61
97|47
75|29
61|13
75|53
29|13
97|29
53|29
61|53
97|53
61|29
47|13
75|47
97|75
47|61
75|61
47|29
75|13
53|13

75,47,61,53,29
97,61,53,29,13
75,29,13
75,97,47,61,53
61,13,29
97,13,75,29,47
            """.trimIndent()
                .lines()
        val result = part1(input)
        assertThat(result).isEqualTo(143)
    }

    @Test
    fun part1Input() {
        val lines = fileInput("Day5.txt").readLines()
        val duration = measureTime {
            val result = part1(lines)
            assertThat(result).isEqualTo(5509)
        }
        println("part1 $duration")
    }

    fun part1(input: List<String>): Int {
        val pairs = mutableListOf<Pair<Int, Int>>()
        val updates = mutableListOf<List<Int>>()
        var pairsEnded = false
        input.forEach { line ->
            if (line.isEmpty()) {
                pairsEnded = true
            } else {
                if (pairsEnded) {
                    updates.add(line.split(',').map { string -> string.toInt() })
                } else {
                    val (first, second) = line.split('|').map(String::toInt)
                    pairs.add(Pair(first, second))
                }
            }
        }
        println("Pairs: ${pairs.size}")
        println("Updates: ${updates.size}")
        val forward = pairs
            .groupBy { pair -> pair.first }
            .mapValues { entry ->
                entry.value.map { pair -> pair.second }.sorted()
            }
            .also(::println)
        println("Pairs: ${pairs.size}")
        val backward = pairs
            .groupBy { pair -> pair.second }
            .mapValues { entry ->
                entry.value.map { pair -> pair.first }.sorted()
            }
            .also(::println)

        return updates.also(::println)
            .sumOf { update: List<Int> ->
                val all = updateIsValid(update, backward, forward)
                return@sumOf if (all) {
                    // find middle
                    update[(update.size - 1) / 2]
                } else {
                    0.toInt()
                }
            }
    }

    @Test
    fun part2Example() {
        val input =
            $"""
47|53
97|13
97|61
97|47
75|29
61|13
75|53
29|13
97|29
53|29
61|53
97|53
61|29
47|13
75|47
97|75
47|61
75|61
47|29
75|13
53|13

75,47,61,53,29
97,61,53,29,13
75,29,13
75,97,47,61,53
61,13,29
97,13,75,29,47
            """.trimIndent()
                .lines()
        val result = part2(input)
        assertThat(result).isEqualTo(123)
    }

    @Test
    fun part2Input() {
        val lines = fileInput("Day5.txt").readLines()
        val duration = measureTime {
            val result = part2(lines)
            assertThat(result).isEqualTo(4407)
        }
        println("part2 $duration")
    }

    fun part2(input: List<String>): Int {
        val pairs = mutableListOf<Pair<Int, Int>>()
        val updates = mutableListOf<List<Int>>()
        var pairsEnded = false
        input.forEach { line ->
            if (line.isEmpty()) {
                pairsEnded = true
            } else {
                if (pairsEnded) {
                    updates.add(line.split(',').map { string -> string.toInt() })
                } else {
                    val (first, second) = line.split('|').map(String::toInt)
                    pairs.add(Pair(first, second))
                }
            }
        }
        println("Pairs: ${pairs.size}")
        println("Updates: ${updates.size}")
        val forward = pairs
            .groupBy { pair -> pair.first }
            .mapValues { entry ->
                entry.value.map { pair -> pair.second }.sorted()
            }
            .also(::println)
        println("Pairs: ${pairs.size}")
        val backward = pairs
            .groupBy { pair -> pair.second }
            .mapValues { entry ->
                entry.value.map { pair -> pair.first }.sorted()
            }
            .also(::println)

        return updates.also(::println)
            .sumOf { update: List<Int> ->
                val all = updateIsValid(update, backward, forward)
                return@sumOf if (all) {
                    // ignore valid ones
                    0.toInt()
                } else {
                    // correct numbers

                    // find broken number & position & remove it from the list
                    val broken = getBroken(update, backward, forward)
                    val listWithoutBroken = update.toMutableList()
                    listWithoutBroken.removeAt(broken.first)
//                    println("Broken $broken listWo: $listWithoutBroken")
                    println("update $update")
                    val candidate = update.sortedWith { o1, o2 ->
                         return@sortedWith when {
                                o2 in backward[o1].orEmpty() -> -1
                                o1 in backward[o2].orEmpty() -> 1
                                else -> 0
                            }
//                        val previous = o1
//                        val previousIsGood =    backward[o2]?.contains(previous) != false
//
//                            val ints = forward[o1]
//                        val nextOnesGood =
//                            ints?.contains(o2) != false
//
//                        println("$o1 $o2 p $previousIsGood n $nextOnesGood")
//                        if (!previousIsGood) {
//                            return@sortedWith 1
//                        }
//                        if (!nextOnesGood) {
//                            return@sortedWith -1
//                        }
//                        return@sortedWith 0
                    }.reversed()
                    val updateIsValid = updateIsValid(candidate, backward, forward)
                    println("candidate $updateIsValid $candidate")
                    // try to add broken number to each position
//                    for (i in 0..listWithoutBroken.size) {
//                        val candidate = buildList {
//                            addAll(listWithoutBroken.subList(0, i))
//                            add(broken.second)
//                            addAll(listWithoutBroken.subList(i, listWithoutBroken.size))
//                        }
//                        val updateIsValid = updateIsValid(candidate, backward, forward)
//                        println("candidate $updateIsValid $candidate")
//                    }

                    // find middle
                    candidate[(candidate.size - 1) / 2]
                }
            }
    }

    private fun updateIsValid(
        update: List<Int>,
        backward: Map<Int, List<Int>>,
        forward: Map<Int, List<Int>>
    ): Boolean {
        val all = update.mapIndexed { index, i ->
            val previousIsGood = if (index == 0) {
                // before check is true
                true
            } else {
                val previous = update.get(index - 1)
                backward[i]?.contains(previous) != false
            }
            val nextOnesGood = if (index == update.size) {
                true
            } else {
                val restNumbers = update.subList(index + 1, update.size)
                val ints = forward[i]
                ints?.containsAll(restNumbers) != false
            }
//            println("prev $previousIsGood next $nextOnesGood: $update")
            previousIsGood && nextOnesGood
        }.all { bool -> bool == true }
        return all
    }

    private fun getBroken(
        update: List<Int>,
        backward: Map<Int, List<Int>>,
        forward: Map<Int, List<Int>>
    ): Pair<Int, Int> {
        update.forEachIndexed { index, i ->
            val previousIsGood = if (index == 0) {
                // before check is true
                true
            } else {
                val previous = update.get(index - 1)
                backward[i]?.contains(previous) != false
            }
            if (previousIsGood == false) {
                return Pair(index, i)
            }
            val nextOnesGood = if (index == update.size) {
                true
            } else {
                val restNumbers = update.subList(index + 1, update.size)
                val ints = forward[i]
                ints?.containsAll(restNumbers) != false
            }
            if (nextOnesGood == false) {
                return Pair(index, i)
            }
        }
        error("Shouldn't happen, but was $update")
    }
}
