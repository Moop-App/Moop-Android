package soup.movie.ui.main.settings

import soup.movie.data.model.Theater

data class SettingsViewState(
        val theaterList: List<Theater>,
        val usePaletteTheme: Boolean,
        val useWebLink: Boolean)
