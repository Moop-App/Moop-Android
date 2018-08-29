package soup.movie.ui.detail

import soup.movie.data.model.Trailer

sealed class DetailViewState {

    object LoadingState : DetailViewState() {

        override fun toString(): String = javaClass.simpleName
    }

    data class DoneState(
            val trailers: List<Trailer>) : DetailViewState()
}
