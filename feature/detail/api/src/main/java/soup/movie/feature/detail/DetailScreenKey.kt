package soup.movie.feature.detail

import kotlinx.serialization.Serializable
import soup.movie.feature.navigator.ScreenKey

sealed interface DetailScreenKey : ScreenKey {

    @Serializable
    data class Movie(val movieId: String) : DetailScreenKey

    @Serializable
    data class Poster(val posterUrl: String) : DetailScreenKey
}
