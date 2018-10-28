package soup.movie.ui.main.now

import io.reactivex.Observable
import soup.movie.data.MoopRepository
import soup.movie.data.model.Movie
import soup.movie.ui.main.movie.MovieListPresenter

class NowPresenter(private val repository: MoopRepository) : MovieListPresenter() {

    override fun getMovieList(clearCache: Boolean): Observable<List<Movie>> =
            repository.getNowList(clearCache).map { it.list }
}
