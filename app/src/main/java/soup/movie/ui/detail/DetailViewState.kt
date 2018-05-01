package soup.movie.ui.detail

import soup.movie.data.model.TimeTable
import soup.movie.data.model.Trailer

sealed class DetailViewState {

    data class LoadingState(
            val isTheaterNotExists: Boolean = true) : DetailViewState()

    data class DoneState(
            val timeTable: TimeTable,
            val trailers: List<Trailer>) : DetailViewState()
}
