package soup.movie.ui.main.theaters

import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.disposables.DisposableContainer
import soup.movie.data.MoobRepository
import soup.movie.settings.impl.TheatersSetting
import soup.movie.ui.BasePresenter
import soup.movie.ui.main.theaters.TheatersViewState.*

class TheatersPresenter(private val repository: MoobRepository,
                        private val theatersSetting: TheatersSetting) :
        BasePresenter<TheatersContract.View>(), TheatersContract.Presenter {

    private val mapReadyRelay: PublishRelay<Unit> = PublishRelay.create()

    override fun initObservable(disposable: DisposableContainer) {
        super.initObservable(disposable)
        disposable.add(mapReadyRelay
                .switchMap { getCodeObservable() }
                .startWith(LoadingState)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { view?.render(it) })
    }

    private fun getCodeObservable(): Observable<TheatersViewState> {
        return repository.getCodeList()
                .map { it.toTheaterList() }
                .map { DoneState(it) }
                .cast(TheatersViewState::class.java)
                .onErrorReturnItem(ErrorState)
    }

    override fun refresh() {
        mapReadyRelay.accept(Unit)
    }
}
