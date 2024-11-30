package aoc2023

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test
import kotlin.time.measureTime

class Day5 {
    @Test
    fun part1Example() {
        val input =
            $$"""
            seeds: 79 14 55 13

            seed-to-soil map:
            50 98 2
            52 50 48

            soil-to-fertilizer map:
            0 15 37
            37 52 2
            39 0 15

            fertilizer-to-water map:
            49 53 8
            0 11 42
            42 0 7
            57 7 4

            water-to-light map:
            88 18 7
            18 25 70

            light-to-temperature map:
            45 77 23
            81 45 19
            68 64 13

            temperature-to-humidity map:
            0 69 1
            1 0 69

            humidity-to-location map:
            60 56 37
            56 93 4
            """.trimIndent()
                .lineSequence()
        val result = part1(input)
        assertThat(result).isEqualTo(35)
    }

    @Test
    fun part1Input() {
        fileInput("Day5.txt").useLines { lineSequence ->
            val duration = measureTime {
                val result = part1(lineSequence)
                assertThat(result).isEqualTo(346433842)
            }
            println("part1 $duration")
        }
    }

    data class SourceToDestination(val source: LongRange, val destination: LongRange)

    private fun part1(input: Sequence<String>): Long {
        val lines = input.toList()

        var seeds = lines.first().substringAfter("seeds: ").split(" ").map(String::toLong).toList()
        val maps = lines.drop(1).fold(mutableListOf<MutableList<SourceToDestination>>()) { acc, line ->
            if (line.isEmpty()) {
                acc.add(mutableListOf())
            } else if (line.endsWith("map:")) {
                // skip
            } else {
                // add numbers to the source to destination map
                var (destinationStart, sourceStart, rangeLength) = line.split(" ")
                    .map(String::toLong)

                acc.last().add(
                    SourceToDestination(
                        LongRange(sourceStart, sourceStart + rangeLength - 1),
                        LongRange(destinationStart, destinationStart + rangeLength - 1)
                    )
                )
            }
            acc
        }

        return seeds.minOf { seed ->
            maps.fold(seed) { source, sourceToDestinations ->
                sourceToDestinations
                    .firstOrNull { sourceToDestination -> sourceToDestination.source.contains(source) }
                    ?.let { sourceToDestination ->
                        val index = source - sourceToDestination.source.start
                        sourceToDestination.destination.start + index
                    } ?: source
            }
        }
    }

    @Test
    fun part2Example() {
        val input =
            $$"""
            seeds: 79 14 55 13

            seed-to-soil map:
            50 98 2
            52 50 48

            soil-to-fertilizer map:
            0 15 37
            37 52 2
            39 0 15

            fertilizer-to-water map:
            49 53 8
            0 11 42
            42 0 7
            57 7 4

            water-to-light map:
            88 18 7
            18 25 70

            light-to-temperature map:
            45 77 23
            81 45 19
            68 64 13

            temperature-to-humidity map:
            0 69 1
            1 0 69

            humidity-to-location map:
            60 56 37
            56 93 4
            """.trimIndent()
                .lineSequence()
        val result = part2(input)
        assertThat(result).isEqualTo(46)
    }

    @Test
    fun part2Input() {
        fileInput("Day5.txt").useLines { lineSequence ->
            val duration = measureTime {
                val result = part2(lineSequence)
                assertThat(result).isEqualTo(60294664)
            }
            println("part2 $duration")
        }
    }

    private fun part2(input: Sequence<String>): Long {
        val lines = input.toList()

        var seeds = lines.first().substringAfter("seeds: ")
            .split(" ")
            .chunked(2)
            .map { (start, length) -> LongRange(start.toLong(), start.toLong() + length.toLong()) }
            .toList()

        val maps = lines
            .drop(1)
            .fold(mutableListOf<MutableList<SourceToDestination>>()) { acc, line ->
                if (line.isEmpty()) {
                    acc.add(mutableListOf())
                } else if (line.endsWith("map:")) {
                    // skip
                } else {
                    // add numbers to the source to destination map
                    var (destinationStart, sourceStart, rangeLength) = line.split(" ")
                        .map(String::toLong)

                    acc.last().add(
                        SourceToDestination(
                            LongRange(sourceStart, sourceStart + rangeLength - 1),
                            LongRange(destinationStart, destinationStart + rangeLength - 1)
                        )
                    )
                }
                acc
            }

        return seeds
            .minOf { seed ->
                seed.minOf { l ->
                    maps.fold(l) { source, sourceToDestinations ->
                        sourceToDestinations
                            .firstOrNull { sourceToDestination -> sourceToDestination.source.contains(source) }
                            ?.let { sourceToDestination ->
                                val index = source - sourceToDestination.source.start
                                sourceToDestination.destination.start + index
                            } ?: source
                    }
                }
            }
    }
}
