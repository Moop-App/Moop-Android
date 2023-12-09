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

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import soup.movie.common.DefaultDispatcher
import soup.movie.log.Logger
import soup.movie.model.TheaterAreaModel
import soup.movie.model.TheaterModel
import javax.inject.Inject

@HiltViewModel
class TheaterEditViewModel @Inject constructor(
    private val manager: TheaterEditManager,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
) : ViewModel() {

    val contentUiModel = flow {
        emit(TheaterEditContentUiModel.LoadingState)
        try {
            manager.loadAsync()
            emit(TheaterEditContentUiModel.DoneState)
        } catch (t: Throwable) {
            Logger.w(t)
            emit(TheaterEditContentUiModel.ErrorState)
        }
    }

    val cgvUiModel = combine(
        manager.asCgvFlow(),
        manager.asSelectedTheaterListFlow(),
    ) { cgv, selectedList ->
        cgv.toUiModel(selectedList)
    }

    val lotteUiModel = combine(
        manager.asLotteFlow(),
        manager.asSelectedTheaterListFlow(),
    ) { lotte, selectedList ->
        lotte.toUiModel(selectedList)
    }

    val megaboxUiModel = combine(
        manager.asMegaboxFlow(),
        manager.asSelectedTheaterListFlow(),
    ) { megabox, selectedList ->
        megabox.toUiModel(selectedList)
    }

    val footerUiModel = manager.asSelectedTheaterListFlow()
        .map { TheaterEditFooterUiModel(it) }

    fun add(theater: TheaterModel): Boolean {
        return manager.add(theater)
    }

    fun remove(theater: TheaterModel) {
        manager.remove(theater)
    }

    fun onConfirmClick() {
        manager.save()
    }

    private suspend fun List<TheaterAreaModel>.toUiModel(selectedList: List<TheaterModel>) =
        withContext(defaultDispatcher) {
            TheaterEditChildUiModel(
                map { theaterArea ->
                    TheaterEditAreaGroupUiModel(
                        title = theaterArea.area.name,
                        theaterList = theaterArea.theaterList.map { theater ->
                            TheaterEditTheaterUiModel(
                                theater = theater,
                                checked = selectedList.any { it.id == theater.id },
                            )
                        },
                    )
                },
            )
        }
}
