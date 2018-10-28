package soup.movie.ui.main.plan

import io.reactivex.Observable
import soup.movie.data.MoopRepository
import soup.movie.data.model.Movie
import soup.movie.settings.impl.MovieFilterSetting
import soup.movie.ui.main.movie.MovieListPresenter

class PlanPresenter(filterSetting: MovieFilterSetting,
                    private val repository: MoopRepository) :
        MovieListPresenter(filterSetting) {

    override fun getMovieList(clearCache: Boolean): Observable<List<Movie>> =
            repository.getPlanList(clearCache).map { it.list }
}
