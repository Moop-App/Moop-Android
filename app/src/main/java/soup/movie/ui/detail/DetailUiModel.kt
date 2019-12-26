package soup.movie.ui.detail

import androidx.annotation.Keep
import soup.movie.data.model.*

@Keep
data class HeaderUiModel(
    val movie: Movie,
    val showTm: Int = 0,
    val nations: List<String> = emptyList(),
    val companys: List<Company> = emptyList()
)

@Keep
class ContentUiModel(
    val items: List<ContentItemUiModel>
)

@Keep
sealed class ContentItemUiModel

@Keep
object HeaderItemUiModel : ContentItemUiModel()

@Keep
class CgvItemUiModel(
    val movieId: CgvMovieId,
    val hasInfo: Boolean,
    val rating: String
) : ContentItemUiModel()

@Keep
class LotteItemUiModel(
    val movieId: LotteMovieId,
    val hasInfo: Boolean,
    val rating: String
) : ContentItemUiModel()

@Keep
class MegaboxItemUiModel(
    val movieId: MegaboxMovieId,
    val hasInfo: Boolean,
    val rating: String
) : ContentItemUiModel()

@Keep
class NaverItemUiModel(
    val rating: String,
    val webLink: Url?
) : ContentItemUiModel()

@Keep
class BoxOfficeItemUiModel(
    val rank: Int,
    val rankDate: String,
    val audience: Int,
    val screenDays: Int,
    val rating: String,
    val webLink: Url?
) : ContentItemUiModel()

@Keep
class ImdbItemUiModel(
    val imdb: String,
    val rottenTomatoes: String,
    val metascore: String,
    val webLink: Url?
) : ContentItemUiModel()

@Keep
class PlotItemUiModel(
    val plot: String
) : ContentItemUiModel() {

    var isExpanded: Boolean = false
}

@Keep
class CastItemUiModel(
    val persons: List<PersonUiModel>
) : ContentItemUiModel()

@Keep
class PersonUiModel(
    val name: String,
    val cast: String,
    val query: String
)

@Keep
class TrailerHeaderItemUiModel(
    val movieTitle: String
) : ContentItemUiModel()

@Keep
class TrailerItemUiModel(
    val trailer: Trailer
) : ContentItemUiModel()

@Keep
class TrailerFooterItemUiModel(
    val movieTitle: String
) : ContentItemUiModel()
