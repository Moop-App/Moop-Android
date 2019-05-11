package soup.movie.ui.detail

import android.net.Uri
import androidx.annotation.Keep
import soup.movie.data.model.Movie
import soup.movie.data.model.Trailer

@Keep
class HeaderUiModel(
    val movie: Movie
)

@Keep
class ContentUiModel(
    val items: List<ContentItemUiModel>
)

@Keep
sealed class ContentItemUiModel(
    open val movie: Movie
)

@Keep
class CgvItemUiModel(
    override val movie: Movie
) : ContentItemUiModel(movie)

@Keep
class LotteItemUiModel(
    override val movie: Movie
) : ContentItemUiModel(movie)

@Keep
class MegaboxItemUiModel(
    override val movie: Movie
) : ContentItemUiModel(movie)

@Keep
class NaverItemUiModel(
    override val movie: Movie
) : ContentItemUiModel(movie)

@Keep
class TrailersItemUiModel(
    override val movie: Movie,
    val trailers: List<Trailer> = emptyList()
) : ContentItemUiModel(movie)

@Keep
class ShareAction(
    val imageUri: Uri,
    val mimeType: String
)

