package soup.movie.ui.theater.edit

import soup.movie.data.model.Theater
import soup.movie.ui.BaseContract

interface TheaterEditContract {

    interface Presenter : BaseContract.Presenter<View> {

        fun onConfirmClicked()

        fun remove(theater: Theater)
    }

    interface View : BaseContract.View {

        fun render(viewState: TheaterEditContentViewState)

        fun render(viewState: TheaterEditFooterViewState)
    }
}
