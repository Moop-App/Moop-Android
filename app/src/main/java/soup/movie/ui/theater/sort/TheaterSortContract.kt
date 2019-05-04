package soup.movie.ui.theater.sort

import soup.movie.ui.LegacyBaseContract

interface TheaterSortContract {

    interface Presenter : LegacyBaseContract.Presenter<View> {

        fun onItemMove(fromPosition: Int, toPosition: Int)

        fun saveSnapshot()
    }

    interface View : LegacyBaseContract.View {

        fun render(viewState: TheaterSortViewState)
    }
}
