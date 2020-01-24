package soup.movie.ui.detail

import androidx.annotation.Keep
import soup.movie.data.model.Company
import soup.movie.data.model.Movie
import soup.movie.data.model.Trailer

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
    val movieId: String,
    val hasInfo: Boolean,
    val rating: String
) : ContentItemUiModel()

@Keep
class LotteItemUiModel(
    val movieId: String,
    val hasInfo: Boolean,
    val rating: String
) : ContentItemUiModel()

@Keep
class MegaboxItemUiModel(
    val movieId: String,
    val hasInfo: Boolean,
    val rating: String
) : ContentItemUiModel()

@Keep
class NaverItemUiModel(
    val rating: String,
    val webLink: String?
) : ContentItemUiModel()

@Keep
class BoxOfficeItemUiModel(
    val rank: Int,
    val rankDate: String,
    val audience: Int,
    val screenDays: Int,
    val rating: String,
    val webLink: String?
) : ContentItemUiModel()

@Keep
class ImdbItemUiModel(
    val imdb: String,
    val rottenTomatoes: String,
    val metascore: String,
    val webLink: String?
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
