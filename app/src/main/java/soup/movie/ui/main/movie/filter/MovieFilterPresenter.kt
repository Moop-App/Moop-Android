package soup.movie.ui.main.movie.filter

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.disposables.DisposableContainer
import io.reactivex.schedulers.Schedulers
import soup.movie.data.model.MovieFilter
import soup.movie.data.model.MovieFilter.Companion.FLAG_THEATER_CGV
import soup.movie.data.model.MovieFilter.Companion.FLAG_THEATER_LOTTE
import soup.movie.data.model.MovieFilter.Companion.FLAG_THEATER_MEGABOX
import soup.movie.settings.impl.MovieFilterSetting
import soup.movie.ui.BasePresenter

class MovieFilterPresenter(private val filterSetting: MovieFilterSetting) :
        BasePresenter<MovieFilterContract.View>(),
        MovieFilterContract.Presenter {

    private var filter: MovieFilter? = null

    override fun initObservable(disposable: DisposableContainer) {
        super.initObservable(disposable)
        disposable.add(filterSetting.asObservable()
                .distinctUntilChanged()
                .subscribeOn(Schedulers.io())
                .doOnNext { filter = it }
                .map { MovieFilterViewState(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { view?.render(it) })
    }

    override fun onCgvFilterChanged(isChecked: Boolean) {
        updateFilter(isChecked, FLAG_THEATER_CGV)
    }

    override fun onLotteFilterChanged(isChecked: Boolean) {
        updateFilter(isChecked, FLAG_THEATER_LOTTE)
    }

    override fun onMegaboxFilterChanged(isChecked: Boolean) {
        updateFilter(isChecked, FLAG_THEATER_MEGABOX)
    }

    private fun updateFilter(isChecked: Boolean, flag: Int) {
        filter?.let {
            val success = if (isChecked) {
                it.addFlag(flag)
            } else {
                it.removeFlag(flag)
            }
            if (success) {
                filterSetting.set(it)
            }
        }
    }
}
