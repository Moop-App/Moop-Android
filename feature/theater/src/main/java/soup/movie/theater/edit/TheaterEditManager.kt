package soup.movie.theater.edit

import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import soup.movie.model.Theater
import soup.movie.model.TheaterArea
import soup.movie.model.TheaterAreaGroup
import soup.movie.model.repository.MoopRepository
import soup.movie.settings.AppSettings

class TheaterEditManager(
    private val repository: MoopRepository,
    private val appSettings: AppSettings
) {

    private val cgvSubject = ConflatedBroadcastChannel<List<TheaterArea>>()
    private val lotteSubject = ConflatedBroadcastChannel<List<TheaterArea>>()
    private val megaboxSubject = ConflatedBroadcastChannel<List<TheaterArea>>()
    private val selectedTheatersChannel = ConflatedBroadcastChannel<List<Theater>>(emptyList())

    private var theaterList: List<Theater> = emptyList()
    private var selectedItemSet: MutableSet<Theater> = mutableSetOf()

    fun asCgvFlow(): Flow<List<TheaterArea>> = cgvSubject.asFlow()

    fun asLotteFlow(): Flow<List<TheaterArea>> = lotteSubject.asFlow()

    fun asMegaboxFlow(): Flow<List<TheaterArea>> = megaboxSubject.asFlow()

    fun asSelectedTheaterListFlow(): Flow<List<Theater>> = selectedTheatersChannel.asFlow()

    suspend fun loadAsync(): TheaterAreaGroup {
        setupSelectedList()
        return repository.getCodeList().also {
            setupTotalList(it)
            cgvSubject.send(it.cgv)
            lotteSubject.send(it.lotte)
            megaboxSubject.send(it.megabox)
        }
    }

    private fun setupTotalList(group: TheaterAreaGroup) {
        theaterList = group.run {
            (cgv + lotte + megabox).flatMap(TheaterArea::theaterList)
        }
    }

    private fun setupSelectedList() {
        selectedItemSet = appSettings.favoriteTheaterList.toMutableSet()
        selectedTheatersChannel.offer(
            selectedItemSet.asSequence()
                .sortedBy { it.type }
                .toList())
    }

    private fun updateSelectedItemCount() {
        selectedTheatersChannel.offer(
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
        appSettings.favoriteTheaterList = theaterList.asSequence()
            .filter {
                selectedItemSet.any { selectedItem ->
                    selectedItem.id == it.id
                }
            }
            .sortedBy { it.type }
            .toList()
    }

    companion object {

        const val MAX_ITEMS = 10
    }
}
