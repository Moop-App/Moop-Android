package soup.movie.ui.theater.edit

import soup.movie.data.model.AreaGroup
import soup.movie.data.model.Theater

data class TheaterEditViewState(
        val areaGroups: List<AreaGroup>,
        val selectedTheaters: List<Theater>)
