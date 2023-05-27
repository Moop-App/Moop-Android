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
package soup.movie.feature.theater.edit

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import soup.movie.data.repository.TheaterRepository
import soup.movie.data.settings.AppSettings
import soup.movie.model.TheaterAreaGroupModel
import soup.movie.model.TheaterAreaModel
import soup.movie.model.TheaterModel

class TheaterEditManager(
    private val repository: TheaterRepository,
    private val appSettings: AppSettings
) {

    private val cgvSubject = MutableStateFlow<List<TheaterAreaModel>>(emptyList())
    private val lotteSubject = MutableStateFlow<List<TheaterAreaModel>>(emptyList())
    private val megaboxSubject = MutableStateFlow<List<TheaterAreaModel>>(emptyList())
    private val selectedTheatersChannel = MutableStateFlow<List<TheaterModel>>(emptyList())

    private var theaterList: List<TheaterModel> = emptyList()
    private var selectedItemSet: MutableSet<TheaterModel> = mutableSetOf()

    fun asCgvFlow(): Flow<List<TheaterAreaModel>> = cgvSubject

    fun asLotteFlow(): Flow<List<TheaterAreaModel>> = lotteSubject

    fun asMegaboxFlow(): Flow<List<TheaterAreaModel>> = megaboxSubject

    fun asSelectedTheaterListFlow(): Flow<List<TheaterModel>> = selectedTheatersChannel

    suspend fun loadAsync(): TheaterAreaGroupModel {
        setupSelectedList()
        return repository.getCodeList().also {
            setupTotalList(it)
            cgvSubject.value = it.cgv
            lotteSubject.value = it.lotte
            megaboxSubject.value = it.megabox
        }
    }

    private fun setupTotalList(group: TheaterAreaGroupModel) {
        theaterList = group.run {
            (cgv + lotte + megabox).flatMap(TheaterAreaModel::theaterList)
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

    fun add(theater: TheaterModel): Boolean {
        val isUnderLimit = selectedItemSet.size < MAX_ITEMS
        if (isUnderLimit) {
            selectedItemSet.add(theater)
            updateSelectedItemCount()
        }
        return isUnderLimit
    }

    fun remove(theater: TheaterModel) {
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
