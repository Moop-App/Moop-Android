package soup.movie.ui.theater.edit

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.disposables.DisposableContainer
import soup.movie.data.TheaterEditManager
import soup.movie.data.model.Theater
import soup.movie.ui.LegacyBasePresenter
import soup.movie.ui.theater.edit.TheaterEditContentViewState.*
import soup.movie.ui.theater.edit.TheaterEditContract.Presenter
import soup.movie.ui.theater.edit.TheaterEditContract.View

class TheaterEditPresenter(private val manager: TheaterEditManager) :
        LegacyBasePresenter<View>(), Presenter {

    override fun initObservable(disposable: DisposableContainer) {
        super.initObservable(disposable)
        disposable.add(manager.loadAsync()
                .map { DoneState }
                .cast(TheaterEditContentViewState::class.java)
                .startWith(LoadingState)
                .onErrorReturnItem(ErrorState)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { view?.render(it) })

        disposable.add(manager.asSelectedTheatersSubject()
                .map { TheaterEditFooterViewState(it) }
                .cast(TheaterEditFooterViewState::class.java)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { view?.render(it) })
    }

    override fun onConfirmClicked() {
        manager.save()
    }

    override fun remove(theater: Theater) {
        manager.remove(theater)
    }
}
