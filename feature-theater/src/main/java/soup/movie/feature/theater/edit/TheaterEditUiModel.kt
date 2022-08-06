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

import androidx.annotation.Keep
import soup.movie.model.Theater

sealed class TheaterEditContentUiModel {

    @Keep
    object LoadingState : TheaterEditContentUiModel()

    @Keep
    object ErrorState : TheaterEditContentUiModel()

    @Keep
    object DoneState : TheaterEditContentUiModel()
}

@Keep
data class TheaterEditChildUiModel(
    val areaGroupList: List<TheaterEditAreaGroupUiModel>
)

@Keep
data class TheaterEditAreaGroupUiModel(
    val title: String,
    val theaterList: List<TheaterEditTheaterUiModel>
)

@Keep
data class TheaterEditTheaterUiModel(
    val theater: Theater,
    val checked: Boolean
)

@Keep
data class TheaterEditFooterUiModel(
    val theaterList: List<Theater>
) {

    fun isFull(): Boolean = theaterList.size >= TheaterEditManager.MAX_ITEMS
}
