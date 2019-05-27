package soup.movie.ui.theme

import androidx.annotation.Keep
import soup.movie.theme.ThemeOption

@Keep
class ThemeOptionUiModel(
    val items: List<ThemeOptionItemUiModel>
)

@Keep
class ThemeOptionItemUiModel(
    val themeOption: ThemeOption
)
