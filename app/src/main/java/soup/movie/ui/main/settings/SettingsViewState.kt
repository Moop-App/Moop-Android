package soup.movie.ui.main.settings

import androidx.annotation.Keep
import soup.movie.data.model.Theater
import soup.movie.data.model.Version

sealed class SettingsViewState {

    @Keep
    data class TheaterListViewState(
        val theaterList: List<Theater>)

    @Keep
    data class VersionViewState(
        val current: Version,
        val latest: Version) {

        fun isLatest(): Boolean = current.versionCode >= latest.versionCode
    }
}
