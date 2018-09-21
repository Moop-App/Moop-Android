package soup.movie.ui.main.settings

import androidx.annotation.Keep
import soup.movie.data.model.Theater
import soup.movie.data.model.Version

@Keep
data class SettingsViewState(
        val theaterList: List<Theater>,
        val usePaletteTheme: Boolean,
        val useWebLink: Boolean,
        val version: Version)
