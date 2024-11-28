package aoc2023

fun gameOfCubes1(input: Sequence<String>): Int =
    input
        .sumOf { line ->
            // skip games with over 12 red, 13 green or  14 blue cubes
            val any =
                line
                    .dropWhile { it != ':' }
                    .splitToSequence(';', ',')
                    .any { ballsCount ->
                        val count = ballsCount.filter { it.isDigit() }.toInt()

                        return@any when {
                            ballsCount.contains("red") -> count > 12
                            ballsCount.contains("green") -> count > 13
                            ballsCount.contains("blue") -> count > 14
                            else -> error("Should not happen, but was $ballsCount")
                        }
                    }

            return@sumOf if (any) {
                0
            } else {
                // game #
                line.removePrefix("Game ").takeWhile { it != ':' }.toInt()
            }
        }

fun gameOfCubes2(input: Sequence<String>): Int =
    input.sumOf { line ->
        var maxGreen = 0
        var maxRed = 0
        var maxBlue = 0

        line
            .dropWhile { it != ':' }
            .splitToSequence(';', ',')
            .forEach { str ->
                val count = str.filter { it.isDigit() }.toInt()

                when {
                    str.contains("red") -> if (count > maxRed) maxRed = count
                    str.contains("green") -> if (count > maxGreen) maxGreen = count
                    str.contains("blue") -> if (count > maxBlue) maxBlue = count
                    else -> error("Should not happen, but was $str")
                }
            }

        maxGreen * maxRed * maxBlue
    }
