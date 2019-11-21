package soup.movie.ui.theater.mode

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import soup.movie.R
import soup.movie.databinding.ActivityTheaterModeBinding
import soup.movie.ui.theater.mode.TheaterModeTileManager.TileState

class TheaterModeTileActivity : AppCompatActivity() {

    private val tileManager = TheaterModeTileManager

    private lateinit var binding: ActivityTheaterModeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_theater_mode)

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
