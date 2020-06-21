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
