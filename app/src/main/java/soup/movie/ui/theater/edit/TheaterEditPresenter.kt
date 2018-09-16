package soup.movie.ui.theater.edit

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.internal.disposables.DisposableContainer
import soup.movie.data.model.AreaGroup
import soup.movie.data.model.Theater
import soup.movie.data.MoobRepository
import soup.movie.settings.impl.TheaterSetting
import soup.movie.ui.BasePresenter
import soup.movie.ui.theater.edit.TheaterEditContract.Presenter
import soup.movie.ui.theater.edit.TheaterEditContract.View
import soup.movie.util.toObservable

class TheaterEditPresenter(private val moobRepository: MoobRepository,
                           private val theaterSetting: TheaterSetting) :
        BasePresenter<View>(), Presenter {

    private var theaterList: List<Theater> = emptyList()

    override fun initObservable(disposable: DisposableContainer) {
        super.initObservable(disposable)
        disposable.add(viewStateObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { view?.render(it) })
    }

    private val viewStateObservable: Observable<TheaterEditViewState>
        get() = Observable.combineLatest(
                allTheatersObservable,
                selectedIdSetObservable,
                BiFunction(::TheaterEditViewState))

    private val allTheatersObservable: Observable<List<AreaGroup>>
        get() = moobRepository.getCodeList()
                .map { it.toAreaGroupList() }
                .doOnNext { saveTheaterList(it) }

    private val selectedIdSetObservable: Observable<Set<String>>
        get() = theaterSetting.get()
                .asSequence()
                .map { it.code }
                .toSet()
                .toObservable()

    private fun saveTheaterList(areaGroupList: List<AreaGroup>) {
        theaterList = areaGroupList.flatMap { it.theaterList }
    }

    override fun onConfirmClicked(selectedIdSet: Set<String>) {
        theaterSetting.set(theaterList
                .asSequence()
                .filter { selectedIdSet.contains(it.code) }
                .sortedBy { it.type }
                .toList())
    }
}
