package soup.movie.ui.main.plan

import androidx.annotation.Keep
import soup.movie.data.model.Movie

sealed class PlanViewState {

    @Keep
    object LoadingState : PlanViewState() {

        override fun toString(): String = javaClass.simpleName
    }

    @Keep
    object ErrorState : PlanViewState() {

        override fun toString(): String = javaClass.simpleName
    }

    @Keep
    data class DoneState(val movies: List<Movie>) : PlanViewState()
}
