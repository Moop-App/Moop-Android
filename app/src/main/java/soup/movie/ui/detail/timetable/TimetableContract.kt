package soup.movie.ui.detail.timetable

import soup.movie.data.model.ScreeningDate
import soup.movie.data.model.TheaterWithTimetable
import soup.movie.ui.BaseContract

interface TimetableContract {

    interface Presenter : BaseContract.Presenter<View> {

        fun onItemClick(item: ScreeningDate)

        fun onItemClick(item: TheaterWithTimetable)
    }

    interface View : BaseContract.View {

        fun render(viewState: TimetableViewState)
    }
}
