package soup.movie.ui.theater.mode

import android.graphics.drawable.Icon
import android.os.Build
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import androidx.annotation.RequiresApi
import soup.movie.R
import soup.movie.ui.theater.mode.TheaterModeTileManager.TileState
import soup.movie.ui.theater.mode.TheaterModeTileManager.TileState.*
import soup.movie.util.toHexString
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
