package soup.movie.ui.main.now

import soup.movie.data.model.Movie

sealed class NowViewState {

    class LoadingState : NowViewState()

    data class DoneState(val movies: List<Movie>) : NowViewState()
}
