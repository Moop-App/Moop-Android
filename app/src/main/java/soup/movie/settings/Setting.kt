package soup.movie.settings

import io.reactivex.Observable

interface Setting<T> {

    fun set(value: T)

    fun get(): T

    fun asObservable(): Observable<T>
}
