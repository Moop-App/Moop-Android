package soup.movie.domain.theater.edit

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.ReplaySubject
import soup.movie.data.repository.MoopRepository
import soup.movie.model.Theater
import soup.movie.model.TheaterArea
import soup.movie.model.TheaterAreaGroup
import soup.movie.settings.impl.TheatersSetting

class TheaterEditManager(
    private val repository: MoopRepository,
    private val theatersSetting: TheatersSetting
) {

    private val cgvSubject: ReplaySubject<List<TheaterArea>> = ReplaySubject.create()
    private val lotteSubject: ReplaySubject<List<TheaterArea>> = ReplaySubject.create()
    private val megaboxSubject: ReplaySubject<List<TheaterArea>> = ReplaySubject.create()
    private val selectedTheatersSubject = BehaviorSubject.createDefault<List<Theater>>(emptyList())

    private var theaterList: List<Theater> = emptyList()
    private var selectedItemSet: MutableSet<Theater> = mutableSetOf()

    fun asCgvObservable(): Observable<List<TheaterArea>> = cgvSubject

    fun asLotteObservable(): Observable<List<TheaterArea>> = lotteSubject

    fun asMegaboxObservable(): Observable<List<TheaterArea>> = megaboxSubject

    fun asSelectedTheatersSubject(): Observable<List<Theater>> = selectedTheatersSubject

    suspend fun loadAsync(): TheaterAreaGroup {
        setupSelectedList()
        return repository.getCodeList().also {
            setupTotalList(it)
            cgvSubject.onNext(it.cgv)
            lotteSubject.onNext(it.lotte)
            megaboxSubject.onNext(it.megabox)
        }
    }

    private fun setupTotalList(group: TheaterAreaGroup) {
        theaterList = group.run {
            (cgv + lotte + megabox).flatMap(TheaterArea::theaterList)
        }
    }

    private fun setupSelectedList() {
        selectedItemSet = theatersSetting.get()
            .asSequence()
            .toMutableSet()
        selectedTheatersSubject.onNext(
            selectedItemSet.asSequence()
                .sortedBy { it.type }
                .toList())
    }

    private fun updateSelectedItemCount() {
        selectedTheatersSubject.onNext(
            theaterList.asSequence()
                .filter {
                    selectedItemSet.any { selectedItem ->
                        selectedItem == it
                    }
                }
                .sortedBy { it.type }
                .toList())
    }

    fun add(theater: Theater): Boolean {
        val isUnderLimit = selectedItemSet.size < MAX_ITEMS
        if (isUnderLimit) {
            selectedItemSet.add(theater)
            updateSelectedItemCount()
        }
        return isUnderLimit
    }

    fun remove(theater: Theater) {
        selectedItemSet.remove(theater)
        updateSelectedItemCount()
    }

    fun save() {
        theatersSetting.set(
            theaterList.asSequence()
                .filter {
                    selectedItemSet.any { selectedItem ->
                        selectedItem.id == it.id
                    }
                }
                .sortedBy { it.type }
                .toList())
    }

    companion object {

        const val MAX_ITEMS = 10
    }
}
