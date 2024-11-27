package aoc2023

fun part1Solution(input: List<String>): Number =
    input
        .sumOf { line ->
            val firstDigitChar = line.first { it.isDigit() }
            val lastDigitChar = line.last { it.isDigit() }
            "$firstDigitChar$lastDigitChar".toInt()
        }

val validDigits =
    listOf<String>(
        "1",
        "2",
        "3",
        "4",
        "5",
        "6",
        "7",
        "8",
        "9",
        "one",
        "two",
        "three",
        "four",
        "five",
        "six",
        "seven",
        "eight",
        "nine",
    )

fun part2Solution(input: List<String>): Number =
    input
        .sumOf { line ->
            val firstDigitChar =
                mapToDigit(line.findAnyOf(validDigits)!!.second)
            val lastDigitChar = mapToDigit(line.findLastAnyOf(validDigits)!!.second)
            "$firstDigitChar$lastDigitChar".toInt()
        }

private fun mapToDigit(find: String): String =
    when (find) {
        "1" -> "1"
        "2" -> "2"
        "3" -> "3"
        "4" -> "4"
        "5" -> "5"
        "6" -> "6"
        "7" -> "7"
        "8" -> "8"
        "9" -> "9"
        "one" -> "1"
        "two" -> "2"
        "three" -> "3"
        "four" -> "4"
        "five" -> "5"
        "six" -> "6"
        "seven" -> "7"
        "eight" -> "8"
        "nine" -> "9"
        else -> error("Shouldn't happen, but was $find")
    }
