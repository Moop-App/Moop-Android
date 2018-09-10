package soup.movie.ui.main.now

import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.disposables.DisposableContainer
import soup.movie.data.source.MoobRepository
import soup.movie.ui.BasePresenter
import soup.movie.ui.main.now.NowContract.Presenter
import soup.movie.ui.main.now.NowContract.View
import soup.movie.ui.main.now.NowViewState.DoneState

class NowPresenter(private val moobRepository: MoobRepository) :
        BasePresenter<View>(), Presenter {

    private val refreshRelay = BehaviorRelay.createDefault(false)

    override fun initObservable(disposable: DisposableContainer) {
        super.initObservable(disposable)
        disposable.add(refreshRelay
                .switchMap { moobRepository.getNowList(it) }
                .map { DoneState(it.list) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { view?.render(it) })
    }

    override fun refresh() {
        refreshRelay.accept(true)
    }
}
