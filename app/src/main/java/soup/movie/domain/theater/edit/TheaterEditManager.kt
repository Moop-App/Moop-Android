package soup.movie.domain.theater.edit

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.ReplaySubject
import soup.movie.data.repository.MoopRepository
import soup.movie.data.model.AreaGroup
import soup.movie.data.model.Theater
import soup.movie.data.model.response.CodeResponse
import soup.movie.settings.impl.TheatersSetting

class TheaterEditManager(
    private val repository: MoopRepository,
    private val theatersSetting: TheatersSetting
) {

    private val cgvSubject: ReplaySubject<List<AreaGroup>> = ReplaySubject.create()
    private val lotteSubject: ReplaySubject<List<AreaGroup>> = ReplaySubject.create()
    private val megaboxSubject: ReplaySubject<List<AreaGroup>> = ReplaySubject.create()
    private val selectedTheatersSubject: BehaviorSubject<List<Theater>> =
        BehaviorSubject.createDefault(emptyList())

    private var theaterList: List<Theater> = emptyList()
    private var selectedItemSet: MutableSet<Theater> = mutableSetOf()

    fun asCgvObservable(): Observable<List<AreaGroup>> = cgvSubject

    fun asLotteObservable(): Observable<List<AreaGroup>> = lotteSubject

    fun asMegaboxObservable(): Observable<List<AreaGroup>> = megaboxSubject

    fun asSelectedTheatersSubject(): Observable<List<Theater>> = selectedTheatersSubject

    suspend fun loadAsync(): CodeResponse {
        setupSelectedList()
        return repository.getCodeList().also {
            setupTotalList(it)
            cgvSubject.onNext(it.cgv)
            lotteSubject.onNext(it.lotte)
            megaboxSubject.onNext(it.megabox)
        }
    }

    private fun setupTotalList(response: CodeResponse) {
        theaterList = response.run {
            (cgv + lotte + megabox).flatMap(AreaGroup::theaterList)
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
