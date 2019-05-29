package soup.movie.domain.main

import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables
import soup.movie.domain.model.MovieFilter
import soup.movie.settings.impl.AgeFilterSetting
import soup.movie.settings.impl.GenreFilterSetting
import soup.movie.settings.impl.TheaterFilterSetting

class GetMovieFilterUseCase(
    private val theaterFilterSetting: TheaterFilterSetting,
    private val ageFilterSetting: AgeFilterSetting,
    private val genreFilterSetting: GenreFilterSetting
) {

    operator fun invoke(): Observable<MovieFilter> {
        return Observables.combineLatest(
            theaterFilterSetting.asObservable(),
            ageFilterSetting.asObservable().distinctUntilChanged(),
            genreFilterSetting.asObservable(),
            ::MovieFilter
        )
    }
}
