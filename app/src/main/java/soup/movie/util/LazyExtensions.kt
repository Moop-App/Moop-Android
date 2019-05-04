@file:Suppress("NOTHING_TO_INLINE")

package soup.movie.util

inline fun <T> lazyFast(noinline initializer: () -> T): Lazy<T> =
    lazy(LazyThreadSafetyMode.NONE, initializer)
