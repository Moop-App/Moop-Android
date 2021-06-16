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
package soup.movie.ui.theater.mode

private typealias Listener = (TheaterModeTileManager.TileState) -> Unit

object TheaterModeTileManager {

    enum class TileState {
        Active, Inactive
    }

    private var listener: Listener? = null

    var tileState: TileState = TileState.Inactive
        set(value) {
            if (field != value) {
                field = value
                listener?.invoke(value)
            }
        }

    fun setListener(listener: Listener?) {
        this.listener = listener
        listener?.invoke(tileState)
    }
}
