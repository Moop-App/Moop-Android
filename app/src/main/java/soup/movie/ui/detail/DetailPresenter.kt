package soup.movie.ui.detail

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.disposables.DisposableContainer
import io.reactivex.subjects.BehaviorSubject
import soup.movie.data.model.Movie
import soup.movie.settings.impl.UsePaletteThemeSetting
import soup.movie.ui.BasePresenter
import soup.movie.ui.detail.DetailContract.Presenter
import soup.movie.ui.detail.DetailContract.View
import soup.movie.ui.detail.DetailViewState.DoneState

class DetailPresenter(private var usePaletteThemeSetting: UsePaletteThemeSetting) :
        BasePresenter<View>(), Presenter {

    private val movieSubject: BehaviorSubject<Movie> = BehaviorSubject.create()

    override fun initObservable(disposable: DisposableContainer) {
        super.initObservable(disposable)
        disposable.add(movieSubject
                .map { it.trailers }
                .map { DoneState(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { view?.render(it) }
        )
    }

    override fun requestData(movie: Movie) {
        movieSubject.onNext(movie)
    }

    override fun usePaletteTheme(): Boolean = usePaletteThemeSetting.get()
}
