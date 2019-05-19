package soup.movie.ui.theater.edit

import androidx.annotation.Keep
import soup.movie.data.model.AreaGroup
import soup.movie.data.model.Theater

@Keep
data class TheaterEditChildUiModel(
    val areaGroupList: List<AreaGroup>,
    val selectedTheaterIdSet: List<Theater>
)
