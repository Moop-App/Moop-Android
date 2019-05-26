package soup.movie.ui.main.movie.filter

import androidx.annotation.Keep

sealed class MovieFilterUiModel

@Keep
data class TheaterFilterUiModel(
    val hasCgv: Boolean,
    val hasLotteCinema: Boolean,
    val hasMegabox: Boolean
) : MovieFilterUiModel()

@Keep
data class AgeFilterUiModel(
    val hasAll: Boolean,
    val has12: Boolean,
    val has15: Boolean,
    val has19: Boolean
) : MovieFilterUiModel()

@Keep
data class GenreFilterUiModel(
    val items: List<GenreFilterItem>
) : MovieFilterUiModel()

@Keep
class GenreFilterItem(
    val name: String,
    val isChecked: Boolean
)
