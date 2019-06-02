package soup.movie.ui.settings

import androidx.annotation.Keep
import soup.movie.data.model.Theater
import soup.movie.data.model.Version
import soup.movie.theme.ThemeOption

sealed class SettingsUiModel

@Keep
data class TheaterSettingUiModel(
    val theaterList: List<Theater>
) : SettingsUiModel()

@Keep
data class VersionSettingUiModel(
    val current: Version,
    val latest: Version,
    val isLatest: Boolean
) : SettingsUiModel()

@Keep
data class ThemeSettingUiModel(
    val themeOption: ThemeOption
)
