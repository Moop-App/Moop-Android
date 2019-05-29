package soup.movie.ui.main.home.filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.Observables
import io.reactivex.schedulers.Schedulers
import soup.movie.data.model.AgeFilter
import soup.movie.data.model.AgeFilter.Companion.FLAG_AGE_12
import soup.movie.data.model.AgeFilter.Companion.FLAG_AGE_15
import soup.movie.data.model.AgeFilter.Companion.FLAG_AGE_19
import soup.movie.data.model.AgeFilter.Companion.FLAG_AGE_ALL
import soup.movie.data.model.GenreFilter
import soup.movie.data.model.TheaterFilter
import soup.movie.data.model.TheaterFilter.Companion.FLAG_THEATER_CGV
import soup.movie.data.model.TheaterFilter.Companion.FLAG_THEATER_LOTTE
import soup.movie.data.model.TheaterFilter.Companion.FLAG_THEATER_MEGABOX
import soup.movie.domain.filter.GetGenreListUseCase
import soup.movie.settings.impl.AgeFilterSetting
import soup.movie.settings.impl.GenreFilterSetting
import soup.movie.settings.impl.TheaterFilterSetting
import soup.movie.ui.BaseViewModel
import javax.inject.Inject

class HomeFilterViewModel @Inject constructor(
    getGenreList: GetGenreListUseCase,
    private val theaterFilterSetting: TheaterFilterSetting,
    private val ageFilterSetting: AgeFilterSetting,
    private val genreFilterSetting: GenreFilterSetting
) : BaseViewModel() {

    private var theaterFilter: TheaterFilter? = null
    private var lastGenreFilter: GenreFilter? = null

    private val _theaterUiModel = MutableLiveData<TheaterFilterUiModel>()
    val theaterUiModel: LiveData<TheaterFilterUiModel>
        get() = _theaterUiModel

    private val _ageUiModel = MutableLiveData<AgeFilterUiModel>()
    val ageUiModel: LiveData<AgeFilterUiModel>
        get() = _ageUiModel

    private val _genreUiModel = MutableLiveData<GenreFilterUiModel>()
    val genreUiModel: LiveData<GenreFilterUiModel>
        get() = _genreUiModel

    init {
        theaterFilterSetting.asObservable()
            .distinctUntilChanged()
            .subscribeOn(Schedulers.io())
            .doOnNext { theaterFilter = it }
            .map { it.toUiModel() }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { _theaterUiModel.value = it }
            .disposeOnCleared()

        ageFilterSetting.asObservable()
            .distinctUntilChanged()
            .subscribeOn(Schedulers.io())
            .map { it.toUiModel() }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { _ageUiModel.value = it }
            .disposeOnCleared()

        Observables
            .combineLatest(
                getGenreList(),
                genreFilterSetting.asObservable()
                    .doOnNext { lastGenreFilter = it }
            ) { allGenre, filter ->
                GenreFilterUiModel(
                    allGenre.map {
                        GenreFilterItem(
                            name = it,
                            isChecked = filter.blacklist.contains(it).not()
                        )
                    }
                )
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { _genreUiModel.value = it }
            .disposeOnCleared()
    }

    private fun TheaterFilter.toUiModel(): TheaterFilterUiModel {
        return TheaterFilterUiModel(
            hasCgv = hasCgv(),
            hasLotteCinema = hasLotteCinema(),
            hasMegabox = hasMegabox()
        )
    }

    private fun AgeFilter.toUiModel(): AgeFilterUiModel {
        return AgeFilterUiModel(
            hasAll = hasAll(),
            has12 = has12(),
            has15 = has15(),
            has19 = has19()
        )
    }

    fun onCgvFilterChanged(isChecked: Boolean) {
        updateTheaterFilter(isChecked, FLAG_THEATER_CGV)
    }

    fun onLotteFilterChanged(isChecked: Boolean) {
        updateTheaterFilter(isChecked, FLAG_THEATER_LOTTE)
    }

    fun onMegaboxFilterChanged(isChecked: Boolean) {
        updateTheaterFilter(isChecked, FLAG_THEATER_MEGABOX)
    }

    private fun updateTheaterFilter(isChecked: Boolean, flag: Int) {
        theaterFilter?.let {
            val success = if (isChecked) {
                it.addFlag(flag)
            } else {
                it.removeFlag(flag)
            }
            if (success) {
                theaterFilterSetting.set(it)
            }
        }
    }

    fun onAgeAllFilterClicked() {
        updateAgeFilter(FLAG_AGE_ALL)
    }

    fun onAge12FilterClicked() {
        updateAgeFilter(FLAG_AGE_ALL or FLAG_AGE_12)
    }

    fun onAge15FilterClicked() {
        updateAgeFilter(FLAG_AGE_ALL or FLAG_AGE_12 or FLAG_AGE_15)
    }

    fun onAge19FilterClicked() {
        updateAgeFilter(FLAG_AGE_ALL or FLAG_AGE_12 or FLAG_AGE_15 or FLAG_AGE_19)
    }

    private fun updateAgeFilter(flags: Int) {
        ageFilterSetting.set(AgeFilter(flags))
    }

    fun onGenreFilterClick(genre: String, isChecked: Boolean) {
        val lastGenreSet = lastGenreFilter?.blacklist?.toMutableSet() ?: return
        val changed = if (isChecked) {
            lastGenreSet.remove(genre)
        } else {
            lastGenreSet.add(genre)
        }
        if (changed) {
            genreFilterSetting.set(GenreFilter(lastGenreSet))
        }
    }
}
