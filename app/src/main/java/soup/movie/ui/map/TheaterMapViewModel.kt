package soup.movie.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import soup.movie.data.MoopRepository
import soup.movie.data.model.Theater
import soup.movie.data.model.response.CodeResponse
import soup.movie.ui.base.BaseViewModel
import javax.inject.Inject

class TheaterMapViewModel @Inject constructor(
    private val repository: MoopRepository
) : BaseViewModel() {

    private val refreshRelay: PublishRelay<Unit> = PublishRelay.create()

    private val _uiModel = MutableLiveData<TheaterMapUiModel>()
    val uiModel: LiveData<TheaterMapUiModel>
        get() = _uiModel

    init {
        refreshRelay
            .switchMap { getCodeObservable() }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(_uiModel::setValue)
            .disposeOnCleared()
    }

    private fun getCodeObservable(): Observable<TheaterMapUiModel> {
        return repository.getCodeList()
            .map { it.toTheaterList() }
            .onErrorReturnItem(emptyList())
            .map { TheaterMapUiModel(it) }
    }

    private fun CodeResponse.toTheaterList(): List<TheaterMarkerUiModel> {
        return cgv.list.flatMap { group ->
            group.theaterList.map {
                CgvMarkerUiModel(
                    areaCode = group.area.code,
                    code = it.code,
                    name = "CGV ${it.name}",
                    lat = it.lat,
                    lng = it.lng
                )
            }
        } + lotte.list.flatMap { group ->
            group.theaterList.map {
                LotteCinemaMarkerUiModel(
                    areaCode = group.area.code,
                    code = it.code,
                    name = "롯데시네마 ${it.name}",
                    lat = it.lat,
                    lng = it.lng
                )
            }
        } + megabox.list.flatMap { group ->
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
        refreshRelay.accept(Unit)
    }
}
