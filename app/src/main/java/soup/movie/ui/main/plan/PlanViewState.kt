package soup.movie.ui.main.plan

import soup.movie.data.model.Movie

sealed class PlanViewState {

    class LoadingState : PlanViewState()

    data class DoneState(val movies: List<Movie>) : PlanViewState()
}
