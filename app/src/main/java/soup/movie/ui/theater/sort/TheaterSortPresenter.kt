package soup.movie.ui.theater.sort

import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.disposables.DisposableContainer
import soup.movie.data.model.Theater
import soup.movie.settings.impl.TheatersSetting
import soup.movie.ui.BasePresenter
import soup.movie.ui.theater.sort.TheaterSortContract.Presenter
import soup.movie.ui.theater.sort.TheaterSortContract.View
import java.util.Collections.swap

class TheaterSortPresenter(private val theatersSetting: TheatersSetting) :
        BasePresenter<View>(), Presenter {

    private lateinit var theatersObservable: BehaviorRelay<List<Theater>>

    private var listSnapshot: MutableList<Theater> = mutableListOf()

    override fun initObservable(disposable: DisposableContainer) {
        super.initObservable(disposable)
        theatersObservable = BehaviorRelay.create()

        disposable.add(theatersObservable
                .distinctUntilChanged()
                .doOnNext { listSnapshot = it.toMutableList() }
                .map { TheaterSortViewState(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { view?.render(it) })

        disposable.add(theatersSetting.asObservable()
                .distinctUntilChanged()
                .subscribe { theatersObservable.accept(it) })
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        swap(listSnapshot, fromPosition, toPosition)
    }

    override fun saveSnapshot() {
        theatersSetting.set(listSnapshot.toList())
    }
}
