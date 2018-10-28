package soup.movie.ui.main.movie

import soup.movie.ui.BaseContract

interface MovieListContract {

    interface Presenter : BaseContract.Presenter<View> {

        fun refresh()
    }

    interface View : BaseContract.View {

        fun render(viewState: MovieListViewState)
    }
}
