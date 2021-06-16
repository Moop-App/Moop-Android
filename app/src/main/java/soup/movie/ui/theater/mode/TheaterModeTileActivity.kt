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

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import soup.movie.databinding.ActivityTheaterModeBinding
import soup.movie.ui.theater.mode.TheaterModeTileManager.TileState
import soup.movie.util.viewBindings

class TheaterModeTileActivity : AppCompatActivity() {

    private val tileManager = TheaterModeTileManager

    private val binding by viewBindings(ActivityTheaterModeBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        updateStateView()

        binding.activeButton.setOnClickListener {
            updateState(TileState.Active)
        }
        binding.inactiveButton.setOnClickListener {
            updateState(TileState.Inactive)
        }
    }

    private fun updateState(newState: TileState) {
        tileManager.tileState = newState
        updateStateView()
    }

    private fun updateStateView() {
        binding.stateView.text = when (tileManager.tileState) {
            TileState.Active -> "Active"
            TileState.Inactive -> "Inactive"
        }
    }
}
