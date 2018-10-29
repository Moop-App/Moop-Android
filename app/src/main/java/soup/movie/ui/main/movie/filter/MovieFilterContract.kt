package soup.movie.ui.main.movie.filter

import soup.movie.ui.BaseContract
import soup.movie.ui.main.movie.filter.MovieFilterViewState.AgeFilterViewState
import soup.movie.ui.main.movie.filter.MovieFilterViewState.TheaterFilterViewState

interface MovieFilterContract {

    interface Presenter : BaseContract.Presenter<View> {

        fun onCgvFilterChanged(isChecked: Boolean)
        fun onLotteFilterChanged(isChecked: Boolean)
        fun onMegaboxFilterChanged(isChecked: Boolean)

        fun onAgeAllFilterClicked()
        fun onAge12FilterClicked()
        fun onAge15FilterClicked()
        fun onAge19FilterClicked()
    }

    interface View : BaseContract.View {

        fun render(viewState: TheaterFilterViewState)

        fun render(viewState: AgeFilterViewState)
    }
}
