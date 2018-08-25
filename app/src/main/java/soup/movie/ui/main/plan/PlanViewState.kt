package soup.movie.ui.main.plan

import soup.movie.data.model.Movie

sealed class PlanViewState {

    object LoadingState : PlanViewState() {

        override fun toString(): String = javaClass.simpleName
    }

    data class DoneState(val movies: List<Movie>) : PlanViewState()
}
