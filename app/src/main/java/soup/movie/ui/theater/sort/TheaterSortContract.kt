package soup.movie.ui.theater.sort

import soup.movie.data.model.Theater
import soup.movie.ui.BaseContract

interface TheaterSortContract {

    interface Presenter : BaseContract.Presenter<View> {

        fun onConfirmClicked(selectedTheaters: List<Theater>)
    }

    interface View : BaseContract.View {

        fun render(viewState: TheaterSortViewState)
    }
}
