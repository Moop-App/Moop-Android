package soup.movie.data

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import soup.movie.data.model.Movie

object MovieSelectManager {

    private val movieSubject: BehaviorSubject<Movie> = BehaviorSubject.create()

    fun asObservable(): Observable<Movie> = movieSubject

    fun getSelectedItem(): Movie? = movieSubject.value

    fun select(item: Movie) {
        movieSubject.onNext(item)
    }
}
