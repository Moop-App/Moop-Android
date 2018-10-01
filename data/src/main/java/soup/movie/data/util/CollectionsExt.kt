package soup.movie.data.util

fun <T> List<T>.firstOr(default: T): T {
    return if (isEmpty()) default else this[0]
}
