package aoc2024

data class Positionm(
    val row: Int,
    val col: Int,
)

fun Positionm.north() = copy(row.dec(), col)

fun Positionm.northEast() = copy(row.dec(), col.dec())

fun Positionm.northWest() = copy(row.dec(), col.inc())

fun Positionm.east() = copy(row, col.dec())

fun Positionm.west() = copy(row, col.inc())

fun Positionm.south() = copy(row.inc(), col)

fun Positionm.southEast() = copy(row.inc(), col.dec())

fun Positionm.southWest() = copy(row.inc(), col.inc())

class Grid<T>(
    val elements: Map<Positionm, T>,
    val lines: List<List<T>>,
    val rowSize: Int,
    val colSize: Int,
)

fun List<String>.toCharGrid(): Grid<Char> {
    val lines = this
    val elements =
        buildMap {
            lines.mapIndexed { row, line ->
                line.mapIndexed { col, char ->
                    put(Positionm(row, col), char)
                }
            }
        }
    val els = lines.map { it.toList() }
    return Grid(elements, els, lines.size, lines[0].length)
}

fun List<String>.toIntGrid(): Grid<Int> {
    val lines = this
    val elements =
        buildMap {
            lines.mapIndexed { row, line ->
                line.mapIndexed { col, char ->
                    put(Positionm(row, col), char.digitToInt())
                }
            }
        }
    val els = lines.map { it.map(Char::digitToInt).toList() }
    return Grid(elements, els, lines.size, lines[0].length)
}

fun <T> Grid<T>.rowIndices() = IntRange(0, rowSize - 1)

fun <T> Grid<T>.columnIndices() = IntRange(0, colSize - 1)

fun <T> Grid<T>.rows(): List<List<T>> = this.lines

fun <T> Grid<T>.columns(): List<List<T>> = this.lines.transpose()

fun <T> Grid<T>.inBounds(position: Positionm): Boolean = position.row in rowIndices() && position.col in columnIndices()

fun <T> Grid<T>.nearbyWindow(position: Positionm): List<Positionm> {
    return listOf(
        position.north(),
        position.east(),
        position.west(),
        position.south(),
    ).mapNotNull { position ->
        return@mapNotNull if (this.elements[position] != null) {
            position
        } else {
            null
        }
    }
}

// flip rows vs columns
fun <T> List<List<T>>.transpose(): List<List<T>> = (this[0].indices).map { i -> (this.indices).map { j -> this[j][i] } }

// backed by Map<Positionm, T> for easy map[Positionm] lookups
// each element
// filter elements
// Positionm (row, col)
// map
// scan
// sliding window; around
// diagonals; anti-diagonals; main diagonal
fun <T> List<List<T>>.diagonalMain(): List<T> = this[0].indices.map { i -> this[i][i] }

// diagonal up (right) without main
fun <T> List<List<T>>.diagonalUp(): List<List<T>> {
    val rowSize = this.size
    val colSize = this[0].size
    val diagonals = mutableListOf<List<T>>()
    // 1 because skipping main diagonal
    for (diagN in 1..colSize - 1) {
        val diagonal = mutableListOf<T>()
        var col = diagN // starting point is shifting right

        for (row in 0..(rowSize - diagN - 1)) { // end row is shrinking
            diagonal.add(this[row][col++])
        }

        diagonals.add(diagonal)
    }
    return diagonals.toList()
}

fun <T> List<List<T>>.diagonalDown(): List<List<T>> {
    val rowSize = this.size
    val colSize = this[0].size
    val diagonals = mutableListOf<List<T>>()
    // 1 because skipping main diagonal
    for (diagN in 1..rowSize - 1) {
        val diagonal = mutableListOf<T>()
        var row = diagN // starting point is shifting down

        for (col in 0..(colSize - diagN - 1)) { // end col is shrinking
            diagonal.add(this[row++][col])
        }

        diagonals.add(diagonal)
    }
    return diagonals.toList()
}

fun <T> List<List<T>>.diagonals(): List<List<T>> {
    val rows = this
    return buildList {
        addAll(rows.diagonalDown())
        add(rows.diagonalMain())
        addAll(rows.diagonalUp())
    }
}
// read from File
// read from lines

// Positionm.north,south,east,west
// rotate 45, 90, 180, 270

// each line could be chars, digits
