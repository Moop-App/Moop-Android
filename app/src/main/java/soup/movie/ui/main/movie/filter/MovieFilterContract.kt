package soup.movie.ui.main.movie.filter

import soup.movie.ui.BaseContract

interface MovieFilterContract {

    interface Presenter : BaseContract.Presenter<View> {

        fun onCgvFilterChanged(isChecked: Boolean)
        fun onLotteFilterChanged(isChecked: Boolean)
        fun onMegaboxFilterChanged(isChecked: Boolean)
    }

    interface View : BaseContract.View {

        fun render(viewState: MovieFilterViewState)
    }
}
