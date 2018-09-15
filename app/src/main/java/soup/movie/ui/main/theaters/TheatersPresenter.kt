package soup.movie.ui.main.theaters

import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.disposables.DisposableContainer
import soup.movie.settings.impl.TheaterSetting
import soup.movie.ui.BasePresenter

class TheatersPresenter(private val theaterSetting: TheaterSetting) :
        BasePresenter<TheatersContract.View>(), TheatersContract.Presenter {

    private val mapReadyRelay: PublishRelay<Unit> = PublishRelay.create()

    override fun initObservable(disposable: DisposableContainer) {
        super.initObservable(disposable)
        disposable.add(mapReadyRelay
                .switchMap { _ -> theaterSetting.asObservable()
                        .map { TheatersViewState(it) }
                        .distinctUntilChanged()
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { view?.render(it) })
    }

    override fun onMapReady() {
        mapReadyRelay.accept(Unit)
    }
}
