package soup.movie.ui.main.theaters

import soup.movie.ui.BaseContract

interface TheatersContract {

    interface Presenter : BaseContract.Presenter<View> {

        fun refresh()
    }

    interface View : BaseContract.View {

        fun render(viewState: TheatersViewState)
    }
}
