package soup.movie.ui.search

import soup.movie.ui.BaseContract

interface SearchContract {

    interface Presenter : BaseContract.Presenter<View> {

        fun searchFor(query: String)
    }

    interface View : BaseContract.View {

        fun render(viewState: SearchViewState)
    }
}
