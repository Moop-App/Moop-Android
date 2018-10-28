package soup.movie.ui.main.plan

import io.reactivex.Observable
import soup.movie.data.MoopRepository
import soup.movie.data.model.Movie
import soup.movie.ui.main.movie.MovieListPresenter

class PlanPresenter(private val repository: MoopRepository) : MovieListPresenter() {

    override fun getMovieList(clearCache: Boolean): Observable<List<Movie>> =
            repository.getPlanList(clearCache).map { it.list }
}
