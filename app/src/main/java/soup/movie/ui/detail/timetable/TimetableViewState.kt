package soup.movie.ui.detail.timetable

import androidx.annotation.Keep
import soup.movie.data.model.ScreeningDate
import soup.movie.data.model.TheaterWithTimetable

@Keep
data class TimetableViewState(
        val screeningDateList: List<ScreeningDate>,
        val theaters: List<TheaterWithTimetable>)
