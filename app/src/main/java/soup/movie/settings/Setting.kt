package soup.movie.settings

import kotlinx.coroutines.flow.Flow

interface Setting<T> {

    fun set(value: T)

    fun get(): T

    fun asFlow(): Flow<T>
}
