package soup.movie.ui.detail

import android.annotation.SuppressLint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.disposables.DisposableContainer
import io.reactivex.schedulers.Schedulers
import soup.movie.R
import soup.movie.data.MovieSelectManager
import soup.movie.data.model.Movie
import soup.movie.data.model.Theater.Companion.TYPE_CGV
import soup.movie.data.model.Theater.Companion.TYPE_LOTTE
import soup.movie.data.model.Theater.Companion.TYPE_MEGABOX
import soup.movie.data.model.Theater.Companion.TYPE_NONE
import soup.movie.settings.impl.UsePaletteThemeSetting
import soup.movie.ui.BasePresenter
import soup.movie.ui.detail.DetailContract.Presenter
import soup.movie.ui.detail.DetailContract.View
import soup.movie.ui.detail.DetailViewState.DoneState
import soup.movie.ui.detail.DetailViewState.ListItem
import soup.movie.util.ImageUriProvider
import java.util.concurrent.TimeUnit

class DetailPresenter(private var usePaletteThemeSetting: UsePaletteThemeSetting,
                      private var imageUriProvider: ImageUriProvider) :
        BasePresenter<View>(), Presenter {

    override fun initObservable(disposable: DisposableContainer) {
        super.initObservable(disposable)
        disposable.add(MovieSelectManager
                .asObservable()
                .subscribeOn(Schedulers.io())
                .delay(500, TimeUnit.MILLISECONDS)
                .map { DoneState(it.toItems()) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { view?.render(it) }
        )
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

    private fun Movie.toItems(): List<ListItem> {
        val list = mutableListOf(
                ListItem(TYPE_CGV, R.layout.item_detail_cgv, this),
                ListItem(TYPE_LOTTE, R.layout.item_detail_lotte, this),
                ListItem(TYPE_MEGABOX, R.layout.item_detail_megabox, this))
        if (hasNaverInfo()) {
            list.add(ListItem(TYPE_NONE, R.layout.item_detail_naver, this))
        }
        list.add(ListItem(TYPE_NONE, R.layout.item_detail_trailers, this, trailers.orEmpty()))
        return list
    }
}
