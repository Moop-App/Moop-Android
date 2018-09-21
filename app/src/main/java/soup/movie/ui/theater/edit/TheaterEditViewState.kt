package soup.movie.ui.theater.edit

import androidx.annotation.Keep
import soup.movie.data.model.AreaGroup

@Keep
data class TheaterEditViewState(
        val areaGroupList: List<AreaGroup>,
        val selectedTheaterIdSet: Set<String>)
