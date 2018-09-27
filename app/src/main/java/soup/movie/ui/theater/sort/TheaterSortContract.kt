package soup.movie.ui.theater.sort

import soup.movie.ui.BaseContract

interface TheaterSortContract {

    interface Presenter : BaseContract.Presenter<View> {

        fun onItemMove(fromPosition: Int, toPosition: Int)

        fun saveSnapshot()
    }

    interface View : BaseContract.View {

        fun render(viewState: TheaterSortViewState)
    }
}
