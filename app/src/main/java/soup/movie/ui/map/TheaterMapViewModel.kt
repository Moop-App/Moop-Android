package soup.movie.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import soup.movie.model.TheaterAreaGroup
import soup.movie.model.repository.MoopRepository
import soup.movie.ui.base.BaseViewModel

class TheaterMapViewModel @AssistedInject constructor(
    private val repository: MoopRepository
) : BaseViewModel() {

    private val _uiModel = MutableLiveData<TheaterMapUiModel>()
    val uiModel: LiveData<TheaterMapUiModel>
        get() = _uiModel

    init {
        viewModelScope.launch {
            _uiModel.value = loadUiModel()
        }
    }

    private suspend fun loadUiModel(): TheaterMapUiModel {
        return withContext(Dispatchers.IO) {
            try {
                TheaterMapUiModel(repository.getCodeList().toTheaterList())
            } catch (t: Throwable) {
                TheaterMapUiModel(emptyList())
            }
        }
    }

    private fun TheaterAreaGroup.toTheaterList(): List<TheaterMarkerUiModel> {
        return cgv.flatMap { group ->
            group.theaterList.map {
                CgvMarkerUiModel(
                    areaCode = group.area.code,
                    code = it.code,
                    name = "CGV ${it.name}",
                    lat = it.lat,
                    lng = it.lng
                )
            }
        } + lotte.flatMap { group ->
            group.theaterList.map {
                LotteCinemaMarkerUiModel(
                    areaCode = group.area.code,
                    code = it.code,
                    name = "롯데시네마 ${it.name}",
                    lat = it.lat,
                    lng = it.lng
                )
            }
        } + megabox.flatMap { group ->
            group.theaterList.map {
                MegaboxMarkerUiModel(
                    areaCode = group.area.code,
                    code = it.code,
                    name = "메가박스 ${it.name}",
                    lat = it.lat,
                    lng = it.lng
                )
            }
        }
    }

    fun onRefresh() {
        viewModelScope.launch {
            _uiModel.value = loadUiModel()
        }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(): TheaterMapViewModel
    }
}
