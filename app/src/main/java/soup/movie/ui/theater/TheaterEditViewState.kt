package soup.movie.ui.theater

import soup.movie.data.model.TheaterCode

data class TheaterEditViewState(
        val allTheaters: List<TheaterCode>,
        val selectedTheaters: List<TheaterCode>)
