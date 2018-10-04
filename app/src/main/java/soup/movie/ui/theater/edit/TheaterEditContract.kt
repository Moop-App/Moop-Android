package soup.movie.ui.theater.edit

import soup.movie.ui.BaseContract

interface TheaterEditContract {

    interface Presenter : BaseContract.Presenter<View> {

        fun onConfirmClicked()
    }

    interface View : BaseContract.View {

        fun render(viewState: TheaterEditViewState)
    }
}
