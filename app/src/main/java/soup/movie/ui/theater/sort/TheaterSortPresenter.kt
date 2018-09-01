package soup.movie.ui.theater.sort

import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.disposables.DisposableContainer
import soup.movie.data.model.Theater
import soup.movie.settings.TheaterSetting
import soup.movie.ui.BasePresenter
import soup.movie.ui.theater.sort.TheaterSortContract.Presenter
import soup.movie.ui.theater.sort.TheaterSortContract.View
import java.util.*

class TheaterSortPresenter(private val theaterSetting: TheaterSetting) :
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

        disposable.add(theaterSetting.asObservable()
                .distinctUntilChanged()
                .subscribe { theatersObservable.accept(it) })
    }

    override fun onConfirmClicked() {
        theaterSetting.set(listSnapshot.toList())
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        Collections.swap(listSnapshot, fromPosition, toPosition)
    }
}
