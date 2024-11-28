package aoc2023

import java.io.File
import java.nio.file.Paths

fun fileInput(fileName: String): File = Paths.get("", "src", "test", "kotlin", "aoc2023", fileName).toFile()
