package soup.movie.ui.theater.edit

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.internal.disposables.DisposableContainer
import soup.movie.data.MoobRepository
import soup.movie.data.model.AreaGroup
import soup.movie.data.model.Theater
import soup.movie.data.request.CodeRequest
import soup.movie.settings.TheaterSetting
import soup.movie.ui.BasePresenter
import soup.movie.ui.theater.edit.TheaterEditContract.Presenter
import soup.movie.ui.theater.edit.TheaterEditContract.View

class TheaterEditPresenter(
        private val moobRepository: MoobRepository,
        private val theaterSetting: TheaterSetting)
    : BasePresenter<View>(), Presenter {

    override fun initObservable(disposable: DisposableContainer) {
        super.initObservable(disposable)
        disposable.add(viewStateObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { view?.render(it) })
    }

    private val viewStateObservable: Observable<TheaterEditViewState>
        get() = Observable.combineLatest(
                allTheatersObservable,
                theaterSetting.asObservable(),
                BiFunction(::TheaterEditViewState))

    private val allTheatersObservable: Observable<List<Theater>>
        get() = moobRepository.getCodeList(CodeRequest)
                .flatMapIterable {
                    (cgv, lotte, megabox) ->
                    val areas = mutableListOf<AreaGroup>()
                    areas.addAll(cgv.list)
//                    areas.addAll(lotte.list)
//                    areas.addAll(megabox.list)
                    areas.flatMap { it.theaterList }.toList()
                }
                .toList()
                .toObservable()

    override fun onConfirmClicked(selectedTheaters: List<Theater>) {
        theaterSetting.set(selectedTheaters)
    }
}
