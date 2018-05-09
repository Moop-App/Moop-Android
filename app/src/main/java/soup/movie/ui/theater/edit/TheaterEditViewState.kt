package soup.movie.ui.theater.edit

import soup.movie.data.model.TheaterCode

data class TheaterEditViewState(
        val allTheaters: List<TheaterCode>,
        val selectedTheaters: List<TheaterCode>)
