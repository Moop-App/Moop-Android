package soup.movie.ui.main.settings

import soup.movie.data.model.Theater
import soup.movie.data.model.Version

data class SettingsViewState(
        val theaterList: List<Theater>,
        val usePaletteTheme: Boolean,
        val useWebLink: Boolean,
        val version: Version)
