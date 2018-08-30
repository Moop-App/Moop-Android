package soup.movie.ui.main.settings

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.disposables.DisposableContainer
import soup.movie.settings.TheaterSetting
import soup.movie.ui.BasePresenter
import soup.movie.ui.main.settings.SettingsContract.Presenter
import soup.movie.ui.main.settings.SettingsContract.View

class SettingsPresenter(private val theaterSetting: TheaterSetting) :
        BasePresenter<View>(), Presenter {

    override fun initObservable(disposable: DisposableContainer) {
        super.initObservable(disposable)
        disposable.add(theaterSetting.asObservable()
                .map { SettingsViewState(it) }
                .distinctUntilChanged()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { view?.render(it) })
    }
}
