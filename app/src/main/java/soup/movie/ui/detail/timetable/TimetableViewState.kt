package soup.movie.ui.detail.timetable

import androidx.annotation.Keep
import soup.movie.data.model.TimeTable

sealed class TimetableViewState {

    @Keep
    object NoTheaterState : TimetableViewState() {

        override fun toString(): String = javaClass.simpleName
    }

    @Keep
    object NoResultState : TimetableViewState() {

        override fun toString(): String = javaClass.simpleName
    }

    @Keep
    data class DataState(
            val timeTable: TimeTable) : TimetableViewState()
}
