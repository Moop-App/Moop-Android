package soup.movie.ui.main.now

import io.reactivex.Observable
import soup.movie.data.MoopRepository
import soup.movie.data.model.Movie
import soup.movie.settings.impl.MovieFilterSetting
import soup.movie.ui.main.movie.MovieListPresenter

class NowPresenter(filterSetting: MovieFilterSetting,
                   private val repository: MoopRepository) :
        MovieListPresenter(filterSetting) {

    override fun getMovieList(clearCache: Boolean): Observable<List<Movie>> {
        return repository.getNowList(clearCache)
                .map { it ->
                    it.list.sortedBy { it.rank }
                }
    }
}
