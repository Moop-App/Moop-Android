package soup.movie.ui.detail.timetable

import soup.movie.data.model.TimeTable

sealed class TimetableViewState {

    object NoTheaterState : TimetableViewState() {

        override fun toString(): String = javaClass.simpleName
    }

    object NoResultState : TimetableViewState() {

        override fun toString(): String = javaClass.simpleName
    }

    data class DataState(
            val timeTable: TimeTable) : TimetableViewState()
}
