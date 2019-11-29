package soup.movie.ui.settings

import androidx.annotation.Keep
import soup.movie.data.model.Theater
import soup.movie.theme.ThemeOption

sealed class SettingsUiModel

@Keep
data class TheaterSettingUiModel(
    val theaterList: List<Theater>
) : SettingsUiModel()

@Keep
data class VersionSettingUiModel(
    val versionCode: Int,
    val versionName: String,
    val isLatest: Boolean
) : SettingsUiModel()

@Keep
data class ThemeSettingUiModel(
    val themeOption: ThemeOption
)
