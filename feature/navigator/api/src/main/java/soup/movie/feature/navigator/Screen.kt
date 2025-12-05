package soup.movie.feature.navigator

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface Screen : NavKey {

    @Serializable
    data object Main : Screen

    @Serializable
    data object Search : Screen

    @Serializable
    data object Settings : Screen

    @Serializable
    data class Detail(val movieId: String) : Screen
}
