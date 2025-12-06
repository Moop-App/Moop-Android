package soup.movie.feature.settings

import kotlinx.serialization.Serializable
import soup.movie.feature.navigator.ScreenKey

sealed interface SettingsScreenKey : ScreenKey {

    @Serializable
    data object Root : SettingsScreenKey

    @Serializable
    data object ThemeOption : SettingsScreenKey
}

