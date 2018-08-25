package soup.movie.ui.main.settings

import soup.movie.data.model.Theater

sealed class SettingsViewState {

    data class DoneState(
            val theaterList: List<Theater>) : SettingsViewState()
}
