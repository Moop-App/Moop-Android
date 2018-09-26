package soup.movie.ui.detail

import androidx.annotation.Keep
import androidx.annotation.LayoutRes
import soup.movie.data.model.Movie
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
            @LayoutRes
            val layoutRes: Int,
            val item: Movie,
            val trailers: List<Trailer> = emptyList())
}
