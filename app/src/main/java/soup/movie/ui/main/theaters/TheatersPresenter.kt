package soup.movie.ui.main.theaters

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.disposables.DisposableContainer
import soup.movie.settings.impl.TheaterSetting
import soup.movie.ui.BasePresenter

class TheatersPresenter(private val theaterSetting: TheaterSetting) :
        BasePresenter<TheatersContract.View>(), TheatersContract.Presenter {

    override fun initObservable(disposable: DisposableContainer) {
        super.initObservable(disposable)
        disposable.add(theaterSetting.asObservable()
                .map { TheatersViewState(it) }
                .distinctUntilChanged()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { view?.render(it) })
    }
}
