package soup.movie.ui.main.movie.filter

import androidx.annotation.Keep
import soup.movie.data.model.AgeFilter
import soup.movie.data.model.TheaterFilter

sealed class MovieFilterUiModel {

    @Keep
    data class TheaterFilterUiModel(
        val filter: TheaterFilter
    ) : MovieFilterUiModel()

    @Keep
    data class AgeFilterUiModel(
        val filter: AgeFilter
    ) : MovieFilterUiModel()
}
