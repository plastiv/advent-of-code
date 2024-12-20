package utils

fun <T> List<T>.head(): T = first()

fun <T> List<T>.tail(): List<T> = subList(1, size)

fun <T> MutableList<T>.swap(
    index1: Int,
    index2: Int,
) {
    val tmp = this[index1]
    this[index1] = this[index2]
    this[index2] = tmp
}

fun <T> List<T>.swap(
    index1: Int,
    index2: Int,
): List<T> {
    val newList = this.toMutableList()
    newList.swap(index1, index2)
    return newList.toList()
}
