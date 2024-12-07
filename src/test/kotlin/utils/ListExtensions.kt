package utils

fun <T> List<T>.head(): T = first()

fun <T> List<T>.tail(): List<T> = subList(1, size)
