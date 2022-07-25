/*
 * Copyright 2021 SOUP
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package soup.movie.theater.edit

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import soup.movie.data.repository.MovieRepository
import soup.movie.model.Theater
import soup.movie.model.TheaterArea
import soup.movie.model.TheaterAreaGroup
import soup.movie.settings.AppSettings

class TheaterEditManager(
    private val repository: MovieRepository,
    private val appSettings: AppSettings
) {

    private val cgvSubject = MutableStateFlow<List<TheaterArea>>(emptyList())
    private val lotteSubject = MutableStateFlow<List<TheaterArea>>(emptyList())
    private val megaboxSubject = MutableStateFlow<List<TheaterArea>>(emptyList())
    private val selectedTheatersChannel = MutableStateFlow<List<Theater>>(emptyList())

    private var theaterList: List<Theater> = emptyList()
    private var selectedItemSet: MutableSet<Theater> = mutableSetOf()

    fun asCgvFlow(): Flow<List<TheaterArea>> = cgvSubject

    fun asLotteFlow(): Flow<List<TheaterArea>> = lotteSubject

    fun asMegaboxFlow(): Flow<List<TheaterArea>> = megaboxSubject

    fun asSelectedTheaterListFlow(): Flow<List<Theater>> = selectedTheatersChannel

    suspend fun loadAsync(): TheaterAreaGroup {
        setupSelectedList()
        return repository.getCodeList().also {
            setupTotalList(it)
            cgvSubject.value = it.cgv
            lotteSubject.value = it.lotte
            megaboxSubject.value = it.megabox
        }
    }

    private fun setupTotalList(group: TheaterAreaGroup) {
        theaterList = group.run {
            (cgv + lotte + megabox).flatMap(TheaterArea::theaterList)
        }
    }

    private fun setupSelectedList() {
        // TODO: Avoid blocking threads on DataStore
        selectedItemSet = runBlocking { appSettings.getFavoriteTheaterList() }.toMutableSet()
        selectedTheatersChannel.value = selectedItemSet.asSequence()
            .sortedBy { it.type }
            .toList()
    }

    private fun updateSelectedItemCount() {
        selectedTheatersChannel.value = theaterList.asSequence()
            .filter {
                selectedItemSet.any { selectedItem ->
                    selectedItem == it
                }
            }
            .sortedBy { it.type }
            .toList()
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
        // TODO: Avoid blocking threads on DataStore
        runBlocking {
            appSettings.setFavoriteTheaterList(
                theaterList.asSequence()
                    .filter {
                        selectedItemSet.any { selectedItem ->
                            selectedItem.id == it.id
                        }
                    }
                    .sortedBy { it.type }
                    .toList()
            )
        }
    }

    companion object {

        const val MAX_ITEMS = 10
    }
}
