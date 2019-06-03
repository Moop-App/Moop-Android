package soup.movie.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.android.schedulers.AndroidSchedulers
import soup.movie.domain.search.SearchMovieUseCase
import soup.movie.ui.base.BaseViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val searchMovie: SearchMovieUseCase
) : BaseViewModel(), SearchUiMapper {

    private val querySubject = BehaviorRelay.create<String>()

    private val _uiModel = MutableLiveData<SearchContentsUiModel>()
    val uiModel: LiveData<SearchContentsUiModel>
        get() = _uiModel

    init {
        querySubject
            .distinctUntilChanged()
            .debounce(300, TimeUnit.MILLISECONDS)
            .switchMap { query ->
                searchMovie(query)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { _uiModel.value = it.toContentsUiModel() }
            .disposeOnCleared()
    }

    fun searchFor(query: String) {
        querySubject.accept(query)
    }
}
