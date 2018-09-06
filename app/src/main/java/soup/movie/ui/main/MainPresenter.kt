package soup.movie.ui.main

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.disposables.DisposableContainer
import soup.movie.settings.impl.LastMainTabSetting
import soup.movie.settings.impl.LastMainTabSetting.Tab
import soup.movie.settings.impl.LastMainTabSetting.Tab.*
import soup.movie.ui.BasePresenter
import soup.movie.ui.main.MainViewState.*

class MainPresenter(private val lastMainTabSetting: LastMainTabSetting) :
        BasePresenter<MainContract.View>(),
        MainContract.Presenter {

    override fun initObservable(disposable: DisposableContainer) {
        super.initObservable(disposable)
        disposable.add(lastMainTabSetting
                .asObservable()
                .distinctUntilChanged()
                .map { mapToViewState(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { view?.render(it) })
    }

    private fun mapToViewState(tabMode: Tab): MainViewState =
            when (tabMode) {
                Now -> NowState
                Plan -> PlanState
                Theaters -> TheatersState
                Settings -> SettingsState
            }

    override fun setCurrentTab(mode: Tab) {
        lastMainTabSetting.set(mode)
    }
}
