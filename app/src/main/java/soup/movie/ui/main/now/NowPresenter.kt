package soup.movie.ui.main.now

import io.reactivex.Observable
import soup.movie.data.MoopRepository
import soup.movie.data.model.Movie
import soup.movie.settings.impl.AgeFilterSetting
import soup.movie.settings.impl.TheaterFilterSetting
import soup.movie.ui.main.movie.MovieListPresenter

class NowPresenter(theaterFilterSetting: TheaterFilterSetting,
                   ageFilterSetting: AgeFilterSetting,
                   private val repository: MoopRepository) :
        MovieListPresenter(theaterFilterSetting, ageFilterSetting) {

    override fun getMovieList(clearCache: Boolean): Observable<List<Movie>> {
        return repository.getNowList(clearCache)
                .map { it ->
                    it.list.sortedBy { it.rank }
                }
    }
}
