package soup.movie.ui.detail.timetable

import soup.movie.data.model.TimeTable

sealed class TimeTableViewState {

    object LoadingState : TimeTableViewState() {

        override fun toString(): String = javaClass.simpleName
    }

    data class DoneState(
            val timeTable: TimeTable) : TimeTableViewState()
}
