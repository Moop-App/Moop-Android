package soup.movie.ui.detail.timetable

import androidx.annotation.Keep
import soup.movie.data.model.ScreeningDate
import soup.movie.data.model.TheaterWithTimetable

sealed class TimetableViewState {

    @Keep
    object LoadingState : TimetableViewState() {

        override fun toString(): String = javaClass.simpleName
    }

    @Keep
    data class DoneState(
            val screeningDateList: List<ScreeningDate>,
            val theaterList: List<TheaterWithTimetable>): TimetableViewState()

    fun hasNoTheaters(): Boolean = when (this) {
        is DoneState -> theaterList.isEmpty()
        else -> false
    }
}
