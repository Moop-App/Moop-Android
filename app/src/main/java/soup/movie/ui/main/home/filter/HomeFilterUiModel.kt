package soup.movie.ui.main.home.filter

import androidx.annotation.Keep

sealed class HomeFilterUiModel

@Keep
data class TheaterFilterUiModel(
    val hasCgv: Boolean,
    val hasLotteCinema: Boolean,
    val hasMegabox: Boolean
) : HomeFilterUiModel()

@Keep
data class AgeFilterUiModel(
    val hasAll: Boolean,
    val has12: Boolean,
    val has15: Boolean,
    val has19: Boolean
) : HomeFilterUiModel()

@Keep
data class GenreFilterUiModel(
    val items: List<GenreFilterItem>
) : HomeFilterUiModel()

@Keep
class GenreFilterItem(
    val name: String,
    val isChecked: Boolean
)
