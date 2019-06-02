package soup.movie.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.android.schedulers.AndroidSchedulers
import soup.movie.data.MoopRepository
import soup.movie.ui.base.BaseViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val repository: MoopRepository
) : BaseViewModel() {

    private val querySubject = BehaviorRelay.create<String>()

    private val _uiModel = MutableLiveData<SearchUiModel>()
    val uiModel: LiveData<SearchUiModel>
        get() = _uiModel

    init {
        querySubject
            .distinctUntilChanged()
            .debounce(300, TimeUnit.MILLISECONDS)
            .switchMap { query ->
                repository.searchMovie(query)
                    .map { SearchUiModel.DoneState(it) }
                    .cast(SearchUiModel::class.java)
                    .startWith(SearchUiModel.LoadingState)
                    .onErrorReturnItem(SearchUiModel.ErrorState)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { _uiModel.value = it }
            .disposeOnCleared()
    }

    fun searchFor(query: String) {
        querySubject.accept(query)
    }
}
