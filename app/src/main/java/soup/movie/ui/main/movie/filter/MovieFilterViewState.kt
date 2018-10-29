package soup.movie.ui.main.movie.filter

import androidx.annotation.Keep
import soup.movie.data.model.AgeFilter
import soup.movie.data.model.TheaterFilter

sealed class MovieFilterViewState {

    @Keep
    data class TheaterFilterViewState(
            val filter: TheaterFilter)

    @Keep
    data class AgeFilterViewState(
            val filter: AgeFilter)
}
