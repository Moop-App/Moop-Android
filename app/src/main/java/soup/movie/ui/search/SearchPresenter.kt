package soup.movie.ui.search

import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.disposables.DisposableContainer
import soup.movie.data.MoopRepository
import soup.movie.ui.LegacyBasePresenter
import soup.movie.ui.search.SearchViewState.*
import timber.log.Timber
import java.util.concurrent.TimeUnit

class SearchPresenter(private val repository: MoopRepository) :
        LegacyBasePresenter<SearchContract.View>(), SearchContract.Presenter {

    private val querySubject = BehaviorRelay.create<String>()

    override fun initObservable(disposable: DisposableContainer) {
        super.initObservable(disposable)
        disposable.add(querySubject
                .distinctUntilChanged()
                .debounce(300, TimeUnit.MILLISECONDS)
                .doOnNext { Timber.d("SOUP query=$it") }
                .switchMap { query ->
                    repository.searchMovie(query)
                            .map { DoneState(it) }
                            .cast(SearchViewState::class.java)
                            .startWith(LoadingState)
                            .onErrorReturnItem(ErrorState)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { view?.render(it) })
    }

    override fun searchFor(query: String) {
        querySubject.accept(query)
    }
}
