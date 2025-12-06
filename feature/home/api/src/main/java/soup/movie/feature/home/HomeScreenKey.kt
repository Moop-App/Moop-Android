package soup.movie.feature.home

import kotlinx.serialization.Serializable
import soup.movie.feature.navigator.ScreenKey

sealed interface HomeScreenKey : ScreenKey {

    @Serializable
    data object Home : HomeScreenKey

    @Serializable
    data object Favorite : HomeScreenKey
}
