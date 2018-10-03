package soup.movie.data.util

import io.reactivex.Observable

fun <T> T.toAnObservable(): Observable<T> = Observable.just(this)
