package soup.movie.ui.main.settings

import soup.movie.data.model.TheaterCode

sealed class SettingsViewState {

    data class DoneState(
            val isHomeTypeVertical: Boolean,
            val theaterList: List<TheaterCode>) : SettingsViewState()
}
