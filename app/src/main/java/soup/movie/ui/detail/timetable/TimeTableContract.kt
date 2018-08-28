package soup.movie.ui.detail.timetable

import soup.movie.data.model.Movie
import soup.movie.ui.BaseContract

interface TimeTableContract {

    interface Presenter : BaseContract.Presenter<View> {

        fun requestData(movie: Movie)
    }

    interface View : BaseContract.View {

        fun render(viewState: TimeTableViewState)
    }
}
