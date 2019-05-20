package soup.movie.ui.main.settings

import androidx.annotation.Keep
import soup.movie.data.model.Theater
import soup.movie.data.model.Version

sealed class SettingsUiModel

@Keep
data class TheaterSettingUiModel(
    val theaterList: List<Theater>
) : SettingsUiModel()

@Keep
data class VersionSettingUiModel(
    val current: Version,
    val latest: Version
) : SettingsUiModel() {

    fun isLatest(): Boolean = current.versionCode >= latest.versionCode
}
