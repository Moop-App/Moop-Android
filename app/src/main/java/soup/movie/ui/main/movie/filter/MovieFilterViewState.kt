package soup.movie.ui.main.movie.filter

import androidx.annotation.Keep
import soup.movie.data.model.MovieFilter

@Keep
data class MovieFilterViewState(
        val filter: MovieFilter)
