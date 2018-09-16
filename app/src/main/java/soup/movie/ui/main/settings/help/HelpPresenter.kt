package soup.movie.ui.main.settings.help

import io.reactivex.internal.disposables.DisposableContainer
import soup.movie.ui.BasePresenter

class HelpPresenter :
        BasePresenter<HelpContract.View>(), HelpContract.Presenter {

    override fun initObservable(disposable: DisposableContainer) {
        super.initObservable(disposable)
    }
}
