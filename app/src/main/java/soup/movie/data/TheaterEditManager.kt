package soup.movie.data

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.ReplaySubject
import soup.movie.data.model.AreaGroup
import soup.movie.data.model.CodeGroup
import soup.movie.data.model.Theater
import soup.movie.data.model.response.CodeResponse
import soup.movie.settings.impl.TheatersSetting

class TheaterEditManager(private val repository: MoobRepository,
                         private val theatersSetting: TheatersSetting) {

    private val cgvSubject: ReplaySubject<CodeGroup> = ReplaySubject.create()
    private val lotteSubject: ReplaySubject<CodeGroup> = ReplaySubject.create()
    private val megaboxSubject: ReplaySubject<CodeGroup> = ReplaySubject.create()
    private val selectedItemCountSubject: BehaviorSubject<Int> =
            BehaviorSubject.createDefault(0)

    private var theaterList: List<Theater> = emptyList()
    private var selectedIdSet: MutableSet<String> = mutableSetOf()

    fun asCgvObservable(): Observable<CodeGroup> = cgvSubject

    fun asLotteObservable(): Observable<CodeGroup> = lotteSubject

    fun asMegaboxObservable(): Observable<CodeGroup> = megaboxSubject

    fun asSelectedItemCountSubject(): Observable<Int> = selectedItemCountSubject

    fun loadAsync(): Observable<CodeResponse> {
        return repository.getCodeList()
                .doOnNext {
                    setup(it.toAreaGroupList())
                    cgvSubject.onNext(it.cgv)
                    lotteSubject.onNext(it.lotte)
                    megaboxSubject.onNext(it.megabox)
                }
    }

    private fun setup(areaGroupList: List<AreaGroup>) {
        theaterList = areaGroupList.flatMap { it.theaterList }
        selectedIdSet = theatersSetting.get()
                .asSequence()
                .map { it.code }
                .toMutableSet()
        updateSelectedItemCount()
    }

    private fun updateSelectedItemCount() {
        selectedItemCountSubject.onNext(selectedIdSet.size)
    }

    fun add(theater: Theater): Boolean {
        val isUnderLimit = selectedIdSet.size < MAX_ITEMS
        if (isUnderLimit) {
            selectedIdSet.add(theater.code)
            updateSelectedItemCount()
        }
        return isUnderLimit
    }

    fun remove(theater: Theater) {
        selectedIdSet.remove(theater.code)
        updateSelectedItemCount()
    }

    fun save() {
        theatersSetting.set(theaterList
            .asSequence()
            .filter { selectedIdSet.contains(it.code) }
            .sortedBy { it.type }
            .toList())
    }

    companion object {

        const val MAX_ITEMS = 10
    }
}
