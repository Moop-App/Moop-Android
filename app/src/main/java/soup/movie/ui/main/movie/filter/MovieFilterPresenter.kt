package soup.movie.ui.main.movie.filter

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.disposables.DisposableContainer
import io.reactivex.schedulers.Schedulers
import soup.movie.data.model.AgeFilter
import soup.movie.data.model.AgeFilter.Companion.FLAG_AGE_12
import soup.movie.data.model.AgeFilter.Companion.FLAG_AGE_15
import soup.movie.data.model.AgeFilter.Companion.FLAG_AGE_19
import soup.movie.data.model.AgeFilter.Companion.FLAG_AGE_ALL
import soup.movie.data.model.TheaterFilter
import soup.movie.data.model.TheaterFilter.Companion.FLAG_THEATER_CGV
import soup.movie.data.model.TheaterFilter.Companion.FLAG_THEATER_LOTTE
import soup.movie.data.model.TheaterFilter.Companion.FLAG_THEATER_MEGABOX
import soup.movie.settings.impl.AgeFilterSetting
import soup.movie.settings.impl.TheaterFilterSetting
import soup.movie.ui.BasePresenter
import soup.movie.ui.main.movie.filter.MovieFilterViewState.AgeFilterViewState
import soup.movie.ui.main.movie.filter.MovieFilterViewState.TheaterFilterViewState

class MovieFilterPresenter(private val theaterFilterSetting: TheaterFilterSetting,
                           private val ageFilterSetting: AgeFilterSetting) :
        BasePresenter<MovieFilterContract.View>(),
        MovieFilterContract.Presenter {

    private var theaterFilter: TheaterFilter? = null

    override fun initObservable(disposable: DisposableContainer) {
        super.initObservable(disposable)
        disposable.add(theaterFilterSetting.asObservable()
                .distinctUntilChanged()
                .subscribeOn(Schedulers.io())
                .doOnNext { theaterFilter = it }
                .map { TheaterFilterViewState(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { view?.render(it) })
        disposable.add(ageFilterSetting.asObservable()
                .distinctUntilChanged()
                .subscribeOn(Schedulers.io())
                .map { AgeFilterViewState(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { view?.render(it) })
    }

    override fun onCgvFilterChanged(isChecked: Boolean) {
        updateTheaterFilter(isChecked, FLAG_THEATER_CGV)
    }

    override fun onLotteFilterChanged(isChecked: Boolean) {
        updateTheaterFilter(isChecked, FLAG_THEATER_LOTTE)
    }

    override fun onMegaboxFilterChanged(isChecked: Boolean) {
        updateTheaterFilter(isChecked, FLAG_THEATER_MEGABOX)
    }

    private fun updateTheaterFilter(isChecked: Boolean, flag: Int) {
        theaterFilter?.let {
            val success = if (isChecked) {
                it.addFlag(flag)
            } else {
                it.removeFlag(flag)
            }
            if (success) {
                theaterFilterSetting.set(it)
            }
        }
    }

    override fun onAgeAllFilterClicked() {
        updateAgeFilter(FLAG_AGE_ALL)
    }

    override fun onAge12FilterClicked() {
        updateAgeFilter(FLAG_AGE_ALL or FLAG_AGE_12)
    }

    override fun onAge15FilterClicked() {
        updateAgeFilter(FLAG_AGE_ALL or FLAG_AGE_12 or FLAG_AGE_15)
    }

    override fun onAge19FilterClicked() {
        updateAgeFilter(FLAG_AGE_ALL or FLAG_AGE_12 or FLAG_AGE_15 or FLAG_AGE_19)
    }

    private fun updateAgeFilter(flags: Int) {
        ageFilterSetting.set(AgeFilter(flags))
    }
}
