package soup.movie.ui.main.now

import soup.movie.data.model.Movie

sealed class NowViewState {

    object LoadingState : NowViewState()

    data class DoneState(val movies: List<Movie>) : NowViewState()
}
