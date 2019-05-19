package soup.movie.ui.main.movie.filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import soup.movie.data.model.AgeFilter
import soup.movie.data.model.AgeFilter.Companion.FLAG_AGE_12
import soup.movie.data.model.AgeFilter.Companion.FLAG_AGE_15
import soup.movie.data.model.AgeFilter.Companion.FLAG_AGE_19
import soup.movie.data.model.AgeFilter.Companion.FLAG_AGE_ALL
import soup.movie.data.model.TheaterFilter
import soup.movie.data.model.TheaterFilter.Companion.FLAG_THEATER_CGV
import soup.movie.data.model.TheaterFilter.Companion.FLAG_THEATER_LOTTE
import soup.movie.data.model.TheaterFilter.Companion.FLAG_THEATER_MEGABOX
import soup.movie.settings.impl.AgeFilterSetting
import soup.movie.settings.impl.TheaterFilterSetting
import soup.movie.ui.BaseViewModel
import soup.movie.ui.main.movie.filter.MovieFilterUiModel.AgeFilterUiModel
import soup.movie.ui.main.movie.filter.MovieFilterUiModel.TheaterFilterUiModel
import javax.inject.Inject

class MovieFilterViewModel @Inject constructor(
    private val theaterFilterSetting: TheaterFilterSetting,
    private val ageFilterSetting: AgeFilterSetting
) : BaseViewModel() {

    private var theaterFilter: TheaterFilter? = null

    private val _theaterUiModel = MutableLiveData<TheaterFilterUiModel>()
    val theaterUiModel: LiveData<TheaterFilterUiModel>
        get() = _theaterUiModel

    private val _ageUiModel = MutableLiveData<AgeFilterUiModel>()
    val ageUiModel: LiveData<AgeFilterUiModel>
        get() = _ageUiModel

    init {
        theaterFilterSetting.asObservable()
            .distinctUntilChanged()
            .subscribeOn(Schedulers.io())
            .doOnNext { theaterFilter = it }
            .map { TheaterFilterUiModel(it) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { _theaterUiModel.value = it }
            .disposeOnCleared()

        ageFilterSetting.asObservable()
            .distinctUntilChanged()
            .subscribeOn(Schedulers.io())
            .map { AgeFilterUiModel(it) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { _ageUiModel.value = it }
            .disposeOnCleared()
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
}
