package soup.movie.ui.theater.sort

import soup.movie.data.model.TheaterCode

data class TheaterSortViewState(
        val allTheaters: List<TheaterCode>,
        val selectedTheaters: List<TheaterCode>)
