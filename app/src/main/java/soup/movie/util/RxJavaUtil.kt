package soup.movie.util

import io.reactivex.Observable

fun <T> T.toObservable(): Observable<T> = Observable.just(this)
