package aoc2024

import java.io.File
import java.nio.file.Paths

fun fileInput(fileName: String): File = Paths.get("", "src", "test", "kotlin", "aoc2024", fileName).toFile()
