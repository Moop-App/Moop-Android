package soup.movie.ui.theater.edit.tab

import soup.movie.ui.LegacyBaseContract

interface TheaterEditChildContract {

    interface Presenter : LegacyBaseContract.Presenter<View>, TheaterEditChildListAdapter.Listener

    interface View : LegacyBaseContract.View {

        fun render(viewState: TheaterEditChildViewState)
    }
}
