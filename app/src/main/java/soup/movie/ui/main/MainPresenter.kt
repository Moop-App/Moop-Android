package soup.movie.ui.main

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.disposables.DisposableContainer
import io.reactivex.subjects.BehaviorSubject
import soup.movie.ui.BasePresenter
import soup.movie.ui.main.MainContract.*
import soup.movie.ui.main.MainContract.Companion.TAB_MODE_NOW
import soup.movie.ui.main.MainContract.Companion.TAB_MODE_PLAN
import soup.movie.ui.main.MainContract.Companion.TAB_MODE_SETTINGS
import soup.movie.ui.main.MainViewState.*

class MainPresenter : BasePresenter<View>(), Presenter {

    private val tabSubject = BehaviorSubject.createDefault(TAB_MODE_NOW)

    override fun initObservable(disposable: DisposableContainer) {
        super.initObservable(disposable)
        disposable.add(tabSubject
                .map { mapToViewState(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { view?.render(it) })
    }

    private fun mapToViewState(@TabMode tabMode: Int): MainViewState {
        return when (tabMode) {
            TAB_MODE_NOW -> NowState
            TAB_MODE_PLAN -> PlanState
            TAB_MODE_SETTINGS -> SettingsState
            else -> throw IllegalStateException("Unknown tab mode")
        }
    }

    override fun setTabMode(@TabMode mode: Int) {
        tabSubject.onNext(mode)
    }
}
