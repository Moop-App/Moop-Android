package soup.movie.ui.main.settings

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.internal.disposables.DisposableContainer
import soup.movie.settings.impl.TheaterSetting
import soup.movie.settings.impl.UsePaletteThemeSetting
import soup.movie.ui.BasePresenter
import soup.movie.ui.main.settings.SettingsContract.Presenter
import soup.movie.ui.main.settings.SettingsContract.View

class SettingsPresenter(private val theaterSetting: TheaterSetting,
                        private val usePaletteThemeSetting: UsePaletteThemeSetting) :
        BasePresenter<View>(), Presenter {

    override fun initObservable(disposable: DisposableContainer) {
        super.initObservable(disposable)
        disposable.add(Observable.combineLatest(
                theaterSetting.asObservable(),
                usePaletteThemeSetting.asObservable(),
                BiFunction(::SettingsViewState))
                .distinctUntilChanged()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { view?.render(it) })
    }

    override fun setPaletteThemeSwitch(checked: Boolean) {
        usePaletteThemeSetting.set(checked)
    }
}
