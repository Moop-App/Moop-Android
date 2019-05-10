package soup.movie.ui.detail

import android.net.Uri
import androidx.annotation.Keep
import androidx.annotation.LayoutRes
import soup.movie.data.model.Movie
import soup.movie.data.model.TheaterType
import soup.movie.data.model.Trailer

sealed class DetailViewState {

    @Keep
    object LoadingState : DetailViewState() {

        override fun toString(): String = javaClass.simpleName
    }

    @Keep
    data class DoneState(
            val items: List<ListItem>) : DetailViewState()

    @Keep
    data class ListItem(
            val type: TheaterType,
            @LayoutRes
            val layoutRes: Int,
            val movie: Movie,
            val trailers: List<Trailer> = emptyList())
}

class ShareAction(
    val imageUri: Uri,
    val mimeType: String
)

