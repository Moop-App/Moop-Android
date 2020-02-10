package soup.movie.ui.home.filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import soup.movie.model.repository.MoopRepository
import soup.movie.settings.AppSettings
import soup.movie.settings.model.AgeFilter
import soup.movie.settings.model.AgeFilter.Companion.FLAG_AGE_12
import soup.movie.settings.model.AgeFilter.Companion.FLAG_AGE_15
import soup.movie.settings.model.AgeFilter.Companion.FLAG_AGE_19
import soup.movie.settings.model.AgeFilter.Companion.FLAG_AGE_ALL
import soup.movie.settings.model.GenreFilter
import soup.movie.settings.model.TheaterFilter
import soup.movie.settings.model.TheaterFilter.Companion.FLAG_THEATER_CGV
import soup.movie.settings.model.TheaterFilter.Companion.FLAG_THEATER_LOTTE
import soup.movie.settings.model.TheaterFilter.Companion.FLAG_THEATER_MEGABOX
import javax.inject.Inject

class HomeFilterViewModel @Inject constructor(
    private val repository: MoopRepository,
    private val appSettings: AppSettings
) : ViewModel() {

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
        appSettings.getTheaterFilterFlow()
            .distinctUntilChanged()
            .onEach {
                theaterFilter = it
                _theaterUiModel.value = it.toUiModel()
            }
            .launchIn(viewModelScope)

        appSettings.getAgeFilterFlow()
            .distinctUntilChanged()
            .onEach { _ageUiModel.value = it.toUiModel() }
            .launchIn(viewModelScope)

        viewModelScope.launch {
            val allGenre = getGenreList()
            appSettings.getGenreFilterFlow()
                .collect { filter ->
                    lastGenreFilter = filter
                    _genreUiModel.value = GenreFilterUiModel(
                        allGenre.map {
                            GenreFilterItem(
                                name = it,
                                isChecked = filter.blacklist.contains(it).not()
                            )
                        }
                    )
                }
        }
    }

    private suspend fun getGenreList(): List<String> {
        return mutableListOf<String>().apply {
            addAll(repository.getGenreList())
            add(GenreFilter.GENRE_ETC)
        }
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
                appSettings.theaterFilter = it
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
        appSettings.ageFilter = AgeFilter(flags)
    }

    fun onGenreFilterClick(genre: String, isChecked: Boolean) {
        val lastGenreSet = lastGenreFilter?.blacklist?.toMutableSet() ?: return
        val changed = if (isChecked) {
            lastGenreSet.remove(genre)
        } else {
            lastGenreSet.add(genre)
        }
        if (changed) {
            appSettings.genreFilter = GenreFilter(lastGenreSet)
        }
    }
}
