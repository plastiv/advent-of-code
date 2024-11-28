package aoc2023

fun sumAllValues(input: List<String>): Number =
    input
        .sumOf { line ->
            val firstDigitChar = line.first { it.isDigit() }
            val lastDigitChar = line.last { it.isDigit() }
            "$firstDigitChar$lastDigitChar".toInt()
        }
