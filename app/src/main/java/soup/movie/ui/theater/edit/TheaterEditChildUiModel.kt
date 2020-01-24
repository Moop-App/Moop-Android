package soup.movie.ui.theater.edit

import androidx.annotation.Keep
import soup.movie.data.model.TheaterArea
import soup.movie.data.model.Theater

@Keep
class TheaterEditChildUiModel(
    val areaGroupList: List<TheaterArea>,
    val selectedTheaterIdSet: List<Theater>
)
