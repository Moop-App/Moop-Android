package soup.movie.theatermap

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import soup.movie.model.TheaterAreaGroup
import soup.movie.model.repository.MoopRepository
import javax.inject.Inject

class TheaterMapViewModel @Inject constructor(
    //private val repository: MoopRepository
) : ViewModel() {

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
                //TODO: Fix this
                //TheaterMapUiModel(repository.getCodeList().toTheaterList())
                TheaterMapUiModel(emptyList())
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
}
