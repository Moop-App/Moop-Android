package soup.movie.ui.theater.sort

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.disposables.DisposableContainer
import soup.movie.data.model.Theater
import soup.movie.settings.TheaterSetting
import soup.movie.ui.BasePresenter
import soup.movie.ui.theater.sort.TheaterSortContract.Presenter
import soup.movie.ui.theater.sort.TheaterSortContract.View

class TheaterSortPresenter(private val theaterSetting: TheaterSetting) :
        BasePresenter<View>(), Presenter {

    override fun initObservable(disposable: DisposableContainer) {
        super.initObservable(disposable)
        disposable.add(theaterSetting.asObservable()
                .map { TheaterSortViewState(it) }
                .distinctUntilChanged()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { view?.render(it) })
    }

    override fun onConfirmClicked(selectedTheaters: List<Theater>) {
        theaterSetting.set(selectedTheaters)
    }
}
