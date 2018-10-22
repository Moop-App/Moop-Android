package soup.movie.ui.main.settings

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.disposables.DisposableContainer
import io.reactivex.rxkotlin.Observables
import soup.movie.data.MoopRepository
import soup.movie.data.helper.VersionHelper.currentVersion
import soup.movie.settings.impl.TheatersSetting
import soup.movie.settings.impl.UsePaletteThemeSetting
import soup.movie.settings.impl.UseWebLinkSetting
import soup.movie.ui.BasePresenter
import soup.movie.ui.main.settings.SettingsContract.Presenter
import soup.movie.ui.main.settings.SettingsContract.View

class SettingsPresenter(private val theatersSetting: TheatersSetting,
                        private val usePaletteThemeSetting: UsePaletteThemeSetting,
                        private val useWebLinkSetting: UseWebLinkSetting,
                        private val repository: MoopRepository) :
        BasePresenter<View>(), Presenter {

    override fun initObservable(disposable: DisposableContainer) {
        super.initObservable(disposable)
        disposable.add(repository.refreshVersion().subscribe())
        disposable.add(Observables.combineLatest(
                theatersSetting.asObservable(),
                usePaletteThemeSetting.asObservable(),
                useWebLinkSetting.asObservable(),
                repository.getVersion()
                        .startWith(currentVersion())
                        .defaultIfEmpty(currentVersion())
                        .onErrorReturnItem(currentVersion())
                        .distinctUntilChanged(),
                ::SettingsViewState)
                .distinctUntilChanged()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { view?.render(it) })
    }

    override fun setUsePaletteTheme(enabled: Boolean) {
        usePaletteThemeSetting.set(enabled)
    }

    override fun setUseWebLink(enabled: Boolean) {
        useWebLinkSetting.set(enabled)
    }
}
