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
class TrailersItemUiModel(
    val movieTitle: String,
    val trailers: List<Trailer> = emptyList()
) : ContentItemUiModel()

@Keep
class ShareAction(
    val imageUri: Uri,
    val mimeType: String
)

