@file:Suppress("NOTHING_TO_INLINE")

package soup.movie.data.util

import io.reactivex.Observable

inline fun <T> T.toObservable(): Observable<T> = Observable.just(this)

inline fun <T> Observable<T>?.orEmpty(): Observable<T> = this ?: Observable.empty()
