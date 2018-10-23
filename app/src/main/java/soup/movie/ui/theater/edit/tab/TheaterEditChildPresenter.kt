package soup.movie.ui.theater.edit.tab

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.disposables.DisposableContainer
import io.reactivex.rxkotlin.Observables
import soup.movie.data.TheaterEditManager
import soup.movie.data.model.AreaGroup
import soup.movie.data.model.Theater
import soup.movie.ui.BasePresenter
import soup.movie.ui.theater.edit.tab.TheaterEditChildContract.Presenter
import soup.movie.ui.theater.edit.tab.TheaterEditChildContract.View
import soup.movie.ui.theater.edit.tab.TheaterEditChildViewState.DoneState

abstract class TheaterEditChildPresenter(private val manager: TheaterEditManager) :
        BasePresenter<View>(), Presenter {

    override fun initObservable(disposable: DisposableContainer) {
        super.initObservable(disposable)
        disposable.add(viewStateObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { view?.render(it) })
    }

    private val viewStateObservable: Observable<TheaterEditChildViewState>
        get() = Observables.combineLatest(
                getAllTheatersObservable(),
                selectedIdSetObservable,
                ::DoneState)

    abstract fun getAllTheatersObservable(): Observable<List<AreaGroup>>

    private val selectedIdSetObservable: Observable<List<Theater>>
        get() = manager.asSelectedTheatersSubject()

    override fun add(theater: Theater): Boolean {
        return manager.add(theater)
    }

    override fun remove(theater: Theater) {
        manager.remove(theater)
    }
}
