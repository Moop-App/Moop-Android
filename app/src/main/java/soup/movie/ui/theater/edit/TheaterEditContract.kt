package soup.movie.ui.theater.edit

import soup.movie.data.model.Theater
import soup.movie.ui.LegacyBaseContract

interface TheaterEditContract {

    interface Presenter : LegacyBaseContract.Presenter<View> {

        fun onConfirmClicked()

        fun remove(theater: Theater)
    }

    interface View : LegacyBaseContract.View {

        fun render(viewState: TheaterEditContentViewState)

        fun render(viewState: TheaterEditFooterViewState)
    }
}
