package soup.movie.ui.detail.timetable

import soup.movie.data.model.TimeTable

sealed class TimeTableViewState {

    object NoTheaterState : TimeTableViewState() {

        override fun toString(): String = javaClass.simpleName
    }

    object NoResultState : TimeTableViewState() {

        override fun toString(): String = javaClass.simpleName
    }

    data class DataState(
            val timeTable: TimeTable) : TimeTableViewState()
}
