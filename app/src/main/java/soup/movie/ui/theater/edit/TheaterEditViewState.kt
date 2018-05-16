package soup.movie.ui.theater.edit

import soup.movie.data.model.Theater

data class TheaterEditViewState(
        val allTheaters: List<Theater>,
        val selectedTheaters: List<Theater>)
