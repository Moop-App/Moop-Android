package soup.movie.ui.main.plan

import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.disposables.DisposableContainer
import soup.movie.data.MoobRepository
import soup.movie.ui.BasePresenter
import soup.movie.ui.main.plan.PlanContract.Presenter
import soup.movie.ui.main.plan.PlanContract.View
import soup.movie.ui.main.plan.PlanViewState.*

class PlanPresenter(private val moobRepository: MoobRepository) :
        BasePresenter<View>(), Presenter {

    private val refreshRelay = BehaviorRelay.createDefault(false)

    override fun initObservable(disposable: DisposableContainer) {
        super.initObservable(disposable)
        disposable.add(refreshRelay
                .switchMap { it -> moobRepository.getPlanList(it)
                        .map { DoneState(it.list) }
                        .cast(PlanViewState::class.java)
                        .startWith(LoadingState)
                        .onErrorReturnItem(ErrorState) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { view?.render(it) })
    }

    override fun refresh() {
        refreshRelay.accept(true)
    }
}
