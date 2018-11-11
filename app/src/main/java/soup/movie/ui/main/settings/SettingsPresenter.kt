package soup.movie.ui.main.settings

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.disposables.DisposableContainer
import soup.movie.data.MoopRepository
import soup.movie.data.helper.VersionHelper.currentVersion
import soup.movie.settings.impl.TheatersSetting
import soup.movie.settings.impl.UseWebLinkSetting
import soup.movie.ui.BasePresenter
import soup.movie.ui.main.settings.SettingsContract.Presenter
import soup.movie.ui.main.settings.SettingsContract.View
import soup.movie.ui.main.settings.SettingsViewState.*

class SettingsPresenter(
        private val theatersSetting: TheatersSetting,
        private val useWebLinkSetting: UseWebLinkSetting,
        private val repository: MoopRepository
) : BasePresenter<View>(), Presenter {

    override fun initObservable(disposable: DisposableContainer) {
        super.initObservable(disposable)
        disposable.add(repository.refreshVersion().subscribe())
        disposable.add(theatersSetting.asObservable()
                .map { TheaterListViewState(it) }
                .distinctUntilChanged()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { view?.render(it) })
        disposable.add(useWebLinkSetting.asObservable()
                .map { ExperimentalViewState(it) }
                .distinctUntilChanged()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { view?.render(it) })
        disposable.add(repository.getVersion()
                .startWith(currentVersion())
                .defaultIfEmpty(currentVersion())
                .onErrorReturnItem(currentVersion())
                .distinctUntilChanged()
                .map { VersionViewState(currentVersion(), it) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { view?.render(it) })
    }

    override fun setUseWebLink(enabled: Boolean) {
        useWebLinkSetting.set(enabled)
    }
}
