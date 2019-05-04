package soup.movie.ui

import io.reactivex.Observable

fun <T> Observable<T>.mapResult(): Observable<Result<T>> {
    return compose { observableSource ->
        observableSource
            .map { Result.Success(it) as Result<T> }
            .onErrorReturn { Result.Failure(it) }
            .startWith(Result.Loading)
    }
}
