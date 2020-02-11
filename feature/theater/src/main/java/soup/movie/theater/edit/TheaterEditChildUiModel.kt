package soup.movie.theater.edit

import androidx.annotation.Keep
import soup.movie.model.Theater
import soup.movie.model.TheaterArea

@Keep
class TheaterEditChildUiModel(
    val areaGroupList: List<TheaterArea>,
    val selectedTheaterIdSet: List<Theater>
)
