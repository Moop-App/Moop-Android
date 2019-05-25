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

    @Keep
    data class GenreFilterUiModel(
        val filterList: List<GenreFilterItem>
    ) : MovieFilterUiModel()
}

@Keep
class GenreFilterItem(
    val name: String,
    val isChecked: Boolean
)
