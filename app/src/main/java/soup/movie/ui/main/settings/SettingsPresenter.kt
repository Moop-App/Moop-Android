package soup.movie.ui.main.settings

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function3
import io.reactivex.internal.disposables.DisposableContainer
import soup.movie.settings.impl.TheaterSetting
import soup.movie.settings.impl.UsePaletteThemeSetting
import soup.movie.settings.impl.UseWebLinkSetting
import soup.movie.ui.BasePresenter
import soup.movie.ui.main.settings.SettingsContract.Presenter
import soup.movie.ui.main.settings.SettingsContract.View

class SettingsPresenter(private val theaterSetting: TheaterSetting,
                        private val usePaletteThemeSetting: UsePaletteThemeSetting,
                        private val useWebLinkSetting: UseWebLinkSetting) :
        BasePresenter<View>(), Presenter {

    override fun initObservable(disposable: DisposableContainer) {
        super.initObservable(disposable)
        disposable.add(Observable.combineLatest(
                theaterSetting.asObservable(),
                usePaletteThemeSetting.asObservable(),
                useWebLinkSetting.asObservable(),
                Function3(::SettingsViewState))
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
