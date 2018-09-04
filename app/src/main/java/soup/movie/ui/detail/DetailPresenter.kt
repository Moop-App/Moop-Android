package soup.movie.ui.detail

import android.annotation.SuppressLint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.disposables.DisposableContainer
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import soup.movie.util.ImageUriProvider
import soup.movie.data.model.Movie
import soup.movie.settings.impl.UsePaletteThemeSetting
import soup.movie.ui.BasePresenter
import soup.movie.ui.detail.DetailContract.Presenter
import soup.movie.ui.detail.DetailContract.View
import soup.movie.ui.detail.DetailViewState.DoneState

class DetailPresenter(private var usePaletteThemeSetting: UsePaletteThemeSetting,
                      private var imageUriProvider: ImageUriProvider) :
        BasePresenter<View>(), Presenter {

    private val movieSubject: BehaviorSubject<Movie> = BehaviorSubject.create()

    override fun initObservable(disposable: DisposableContainer) {
        super.initObservable(disposable)
        disposable.add(movieSubject
                .map { it.trailers.orEmpty() }
                .map { DoneState(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { view?.render(it) }
        )
    }

    override fun requestData(movie: Movie) {
        movieSubject.onNext(movie)
    }

    @SuppressLint("CheckResult")
    override fun requestShareImage(url: String) {
        imageUriProvider(url)
                .subscribeOn(Schedulers.io())
                .subscribe { view?.doShareImage(it, getImageMimeType(url)) }
    }

    private fun getImageMimeType(fileName: String): String = when {
        fileName.endsWith(".png") -> "image/png"
        fileName.endsWith(".gif") -> "image/gif"
        else -> "image/jpeg"
    }

    override fun usePaletteTheme(): Boolean = usePaletteThemeSetting.get()
}
