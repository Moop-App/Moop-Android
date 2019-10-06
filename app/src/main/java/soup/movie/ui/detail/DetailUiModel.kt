package soup.movie.ui.detail

import android.net.Uri
import androidx.annotation.Keep
import soup.movie.data.model.*

@Keep
class HeaderUiModel(
    val movie: Movie
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

@Keep
class ShareAction(
    val imageUri: Uri,
    val mimeType: String
)

