package soup.movie.theme

import androidx.annotation.Keep

@Keep
class ThemeSettingUiModel(
    val items: List<ThemeSettingItemUiModel>
)

@Keep
class ThemeSettingItemUiModel(
    val themeOption: ThemeOption
)
