package soup.movie.ui.theater.sort

import androidx.annotation.Keep
import soup.movie.data.model.Theater

@Keep
data class TheaterSortViewState(
        val selectedTheaters: List<Theater>)
