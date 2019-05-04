package soup.movie.ui.main.movie

import soup.movie.ui.LegacyBaseContract

interface MovieListContract {

    interface Presenter : LegacyBaseContract.Presenter<View> {

        fun refresh()
    }

    interface View : LegacyBaseContract.View {

        fun render(viewState: MovieListViewState)
    }
}
