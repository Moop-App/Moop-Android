package soup.movie.ui.theater.edit.tab

import soup.movie.ui.BaseContract

interface TheaterEditChildContract {

    interface Presenter : BaseContract.Presenter<View>, TheaterEditChildListAdapter.Listener

    interface View : BaseContract.View {

        fun render(viewState: TheaterEditChildViewState)
    }
}
