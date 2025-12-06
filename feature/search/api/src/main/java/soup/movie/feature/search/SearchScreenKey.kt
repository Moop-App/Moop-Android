package soup.movie.feature.search

import kotlinx.serialization.Serializable
import soup.movie.feature.navigator.ScreenKey

sealed interface SearchScreenKey : ScreenKey {

    @Serializable
    data object Root : SearchScreenKey
}
