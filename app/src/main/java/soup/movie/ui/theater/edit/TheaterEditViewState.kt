package soup.movie.ui.theater.edit

import soup.movie.data.model.AreaGroup

data class TheaterEditViewState(
        val areaGroupList: List<AreaGroup>,
        val selectedTheaterIdSet: Set<String>)
