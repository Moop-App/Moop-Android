package soup.movie.ui.search

import soup.movie.ui.LegacyBaseContract

interface SearchContract {

    interface Presenter : LegacyBaseContract.Presenter<View> {

        fun searchFor(query: String)
    }

    interface View : LegacyBaseContract.View {

        fun render(viewState: SearchViewState)
    }
}
