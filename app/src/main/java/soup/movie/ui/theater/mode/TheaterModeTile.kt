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

import android.graphics.drawable.Icon
import android.os.Build
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import androidx.annotation.RequiresApi
import soup.movie.R
import soup.movie.ext.toHexString
import soup.movie.ui.theater.mode.TheaterModeTileManager.TileState
import soup.movie.ui.theater.mode.TheaterModeTileManager.TileState.Active
import soup.movie.ui.theater.mode.TheaterModeTileManager.TileState.Inactive
import timber.log.Timber

@RequiresApi(Build.VERSION_CODES.N)
class TheaterModeTile : TileService() {

    private val tileManager = TheaterModeTileManager

    override fun onCreate() {
        super.onCreate()
        Timber.d("onCreate: 0x{${hashCode().toHexString()}}")
    }

    override fun onDestroy() {
        Timber.d("onDestroy: 0x{${hashCode().toHexString()}}")
        super.onDestroy()
    }

    override fun onStartListening() {
        super.onStartListening()
        tileManager.setListener { updateTile(it) }
    }

    override fun onStopListening() {
        super.onStopListening()
        tileManager.setListener(null)
    }

    override fun onClick() {
        tileManager.tileState = when (tileManager.tileState) {
            Active -> Inactive
            Inactive -> Active
        }
    }

    private fun updateTile(tileState: TileState) {
        qsTile.apply {
            when (tileState) {
                Active -> {
                    state = Tile.STATE_ACTIVE
                    icon = Icon.createWithResource(applicationContext, R.drawable.ic_tile_active)
                    label = "Active"
                    TheaterModeService.start(this@TheaterModeTile)
                }
                Inactive -> {
                    state = Tile.STATE_INACTIVE
                    icon = Icon.createWithResource(applicationContext, R.drawable.ic_tile_inactive)
                    label = "Inactive"
                    TheaterModeService.stop(this@TheaterModeTile)
                }
            }
            updateTile()
        }
    }
}
