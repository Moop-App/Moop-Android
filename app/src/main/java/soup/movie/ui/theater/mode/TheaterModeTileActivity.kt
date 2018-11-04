package soup.movie.ui.theater.mode

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_theater_mode.*
import soup.movie.R
import soup.movie.ui.theater.mode.TheaterModeTileManager.TileState

class TheaterModeTileActivity : AppCompatActivity() {

    private val tileManager = TheaterModeTileManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_theater_mode)

        updateStateView()

        activeButton.setOnClickListener {
            updateState(TileState.Active)
        }
        inactiveButton.setOnClickListener {
            updateState(TileState.Inactive)
        }
    }

    private fun updateState(newState: TileState) {
        tileManager.tileState = newState
        updateStateView()
    }

    private fun updateStateView() {
        stateView.text = when (tileManager.tileState) {
            TileState.Active -> "Active"
            TileState.Inactive -> "Inactive"
        }
    }
}
