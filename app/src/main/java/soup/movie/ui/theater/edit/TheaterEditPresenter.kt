package soup.movie.ui.theater.edit

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.disposables.DisposableContainer
import io.reactivex.rxkotlin.Observables
import soup.movie.data.TheaterEditManager
import soup.movie.ui.BasePresenter
import soup.movie.ui.theater.edit.TheaterEditContract.Presenter
import soup.movie.ui.theater.edit.TheaterEditContract.View
import  soup.movie.ui.theater.edit.TheaterEditViewState.*

class TheaterEditPresenter(private val manager: TheaterEditManager) :
        BasePresenter<View>(), Presenter {

    override fun initObservable(disposable: DisposableContainer) {
        super.initObservable(disposable)
        disposable.add(Observables.combineLatest(
                manager.loadAsync(),
                manager.asSelectedTheatersSubject())
                .map { DoneState(it.second) }
                .cast(TheaterEditViewState::class.java)
                .startWith(LoadingState)
                .onErrorReturnItem(ErrorState)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { view?.render(it) })
    }

    override fun onConfirmClicked() {
        manager.save()
    }
}
