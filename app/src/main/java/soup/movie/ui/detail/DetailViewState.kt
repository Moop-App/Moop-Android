package soup.movie.ui.detail

import androidx.annotation.Keep
import soup.movie.data.model.Trailer

sealed class DetailViewState {

    @Keep
    object LoadingState : DetailViewState() {

        override fun toString(): String = javaClass.simpleName
    }

    @Keep
    data class DoneState(
            val trailers: List<Trailer>) : DetailViewState()
}
