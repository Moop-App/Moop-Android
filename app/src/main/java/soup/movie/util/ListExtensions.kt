@file:Suppress("NOTHING_TO_INLINE")

package soup.movie.util

fun <E> MutableList<E>.swap(i: Int, j: Int) {
    // instead of using a raw type here, it's possible to capture
    // the wildcard but it will require a call to a supplementary
    // private method
    this[i] = set(j, this[i])
}
