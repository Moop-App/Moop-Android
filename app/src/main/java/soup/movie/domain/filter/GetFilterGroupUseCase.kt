package soup.movie.domain.filter

import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables
import soup.movie.settings.impl.AgeFilterSetting
import soup.movie.settings.impl.GenreFilterSetting
import soup.movie.settings.impl.TheaterFilterSetting

class GetFilterGroupUseCase(
    private val theaterFilterSetting: TheaterFilterSetting,
    private val ageFilterSetting: AgeFilterSetting,
    private val genreFilterSetting: GenreFilterSetting
) {

    fun asObservable(): Observable<FilterGroup> {
        return Observables.combineLatest(
            theaterFilterSetting.asObservable(),
            ageFilterSetting.asObservable().distinctUntilChanged(),
            genreFilterSetting.asObservable(),
            ::FilterGroup
        )
    }
}
