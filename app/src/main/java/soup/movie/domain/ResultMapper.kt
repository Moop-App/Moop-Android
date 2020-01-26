package soup.movie.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

interface ResultMapper {

    fun <T> Flow<T>.mapResult(): Flow<Result<T>> {
        return map { Result.Success(it) as Result<T> }
            .catch { emit(Result.Failure(it)) }
            .onStart { emit(Result.Loading) }
    }
}
