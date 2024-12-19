package utils

// https://elizarov.medium.com/programming-binary-search-6e999783ba5d
fun IntRange.binarySearchLowerBound(ok: (Int) -> Boolean): Int {
    var l = -1
    var r = this.endInclusive
    while (l + 1 < r) {
        val m = (l + r) ushr 1
        if (ok(m)) {
            r = m
        } else {
            l = m
        }
    }
    return r
}
