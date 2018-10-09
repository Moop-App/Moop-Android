package soup.movie.ui.main

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.disposables.DisposableContainer
import soup.movie.data.MoopRepository
import soup.movie.settings.impl.LastMainTabSetting
import soup.movie.settings.impl.LastMainTabSetting.Tab
import soup.movie.settings.impl.LastMainTabSetting.Tab.*
import soup.movie.ui.BasePresenter
import soup.movie.ui.main.MainActionState.NotFoundAction
import soup.movie.ui.main.MainActionState.ShowDetailAction
import soup.movie.ui.main.MainViewState.*
import timber.log.Timber

class MainPresenter(private val lastMainTabSetting: LastMainTabSetting,
                    private val repository: MoopRepository) :
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
        Timber.d("setCurrentTab: %s", mode)
        lastMainTabSetting.set(mode)
    }

    override fun requestMovie(movieId: String) {
        //TODO: connect with view lifecycle
        repository.getMovie(movieId)
                .observeOn(AndroidSchedulers.mainThread())
                .map<MainActionState> { ShowDetailAction(it) }
                .defaultIfEmpty(NotFoundAction)
                .subscribe { view?.execute(it) }
    }
}
