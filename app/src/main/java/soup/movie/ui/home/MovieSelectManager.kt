package soup.movie.ui.home

import io.reactivex.subjects.BehaviorSubject
import soup.movie.model.Movie

object MovieSelectManager {

    private val movieSubject: BehaviorSubject<Movie> = BehaviorSubject.create()

    fun getSelectedItem(): Movie? = movieSubject.value

    fun select(item: Movie) {
        movieSubject.onNext(item)
    }
}
